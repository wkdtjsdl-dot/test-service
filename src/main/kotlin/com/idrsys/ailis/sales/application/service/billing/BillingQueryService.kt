package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.external.ReqServicePort
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.usecase.billing.BillingQueryUseCase
import com.idrsys.ailis.sales.shared.mapper.toDemandResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Billing Query Service
 *
 * Implements read operations for billing domain
 */
@Service
@Transactional(readOnly = true)
class BillingQueryService(
    private val demandRepository: DemandRepository,
    private val reqServicePort: ReqServicePort,
    private val custCustomRepository: CustCustomRepository,
    private val baseServicePort: BaseServicePort
) : BillingQueryUseCase {

    /**
     * Get demand list (settled or unsettled)
     *
     * Business Rules:
     * 1. SETTLED: Demands that have been created (청구 완료) - from sales-service DB with custNm from JOIN
     * 2. UNSETTLED: Test requests not yet included in demands (미청구) - from req-service API with custNm from batch query
     */
    override fun getDemandList(searchParam: DemandSearchParam): Flow<DemandResponse> {
        return when (searchParam.clcdYn) {
            CLCD.CLCD_Y -> {
                // Get created demands from sales-service DB with custNm (JOIN query)
                demandRepository.findDemandsWithCustInfo(searchParam)
                    .map { DemandResponse.from(it) }
            }
            CLCD.CLCD_N -> {
                // Get unbilled demand summary from req-service API with custNm from batch query
                getUnbilledDemandSummaryFromReqService(searchParam)
            }
        }
    }

    /**
     * Get unbilled demand summary from req-service
     *
     * Business Rules:
     * 1. Determine directAcctCds based on frgnAcctYn and custCd:
     *    - custCd specified: use that single code
     *    - frgnAcctYn=true (foreign only): query foreign direct account codes
     *    - frgnAcctYn=false (domestic only): null (all), then filter out foreign in sales-service
     *    - frgnAcctYn=null (all): null (no filter)
     * 2. Call req-service API to get unbilled test items (closingCd = "CLCD_N")
     * 3. For domestic only: filter out foreign accounts in sales-service
     * 4. Batch query custNm from scs_cust_mst by distinct custCd list
     * 5. Map response to DemandResponse format with custNm
     * 6. Return empty flow if req-service call fails
     */
    private fun getUnbilledDemandSummaryFromReqService(
        searchParam: DemandSearchParam
    ): Flow<DemandResponse> = flow {
        val branchCd = searchParam.branchCd?.takeIf { it.isNotBlank() }

        // Determine custCds based on frgnAcctYn, custCd, and branchCd
        val custCds: List<String>? = when {
            !searchParam.custCd.isNullOrBlank() -> listOf(searchParam.custCd)
            searchParam.frgnAcctYn == true -> custCustomRepository.findCustCdsByFrgnAcctYn(true, branchCd)
            branchCd != null -> {
                // frgnAcctYn이 null(전체) 또는 false(국내)이고 branchCd만 있는 경우
                // 해당 영업소의 국내+해외 계정 모두 조회 (frgnAcctYn 필터는 이후 in-memory에서 처리)
                custCustomRepository.findCustCdsByFrgnAcctYn(false, branchCd) +
                    custCustomRepository.findCustCdsByFrgnAcctYn(true, branchCd)
            }
            else -> null
        }

        // 영업소 필터링 결과 매칭 계정이 없으면 빈 결과 반환
        if (custCds != null && custCds.isEmpty()) return@flow

        val summaries = reqServicePort.getUnbilledDemandSummary(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            custCds = custCds,
        )

        // For domestic only (frgnAcctYn=false): filter out foreign accounts
        val filteredSummaries = if (searchParam.frgnAcctYn == false) {
            val foreignAcctCds = custCustomRepository.findCustCdsByFrgnAcctYn(true).toSet()
            summaries.filter { it.custCd !in foreignAcctCds }
        } else {
            summaries
        }

        // Exclude customers belonging to branch_bcd='100' departments from billing closing
        val branch100DeptCds = baseServicePort.getDeptCdsByBranchBcd("100")
        val branch100CustCds = if (branch100DeptCds.isNotEmpty()) {
            custCustomRepository.findCustCdsByBzoffiCds(branch100DeptCds)
        } else {
            emptySet()
        }
        val finalSummaries = if (branch100CustCds.isNotEmpty()) {
            filteredSummaries.filter { it.custCd !in branch100CustCds }
        } else {
            filteredSummaries
        }

        // Exclude bill_publ_yn=false accounts
        val noBillPublCustCds = custCustomRepository.findNoBillPublCustCds()
        val preBillSummaries = if (noBillPublCustCds.isNotEmpty()) {
            finalSummaries.filter { it.custCd !in noBillPublCustCds }
        } else {
            finalSummaries
        }

        // Post-aggregate by rprs_cust_cd billing key
        val custCdList = preBillSummaries.map { it.custCd }.distinct()
        val rprsBillingInfoMap = custCustomRepository.findRprsBillingInfoByCustCds(custCdList)

        val billPublSummaries = preBillSummaries
            .groupBy { summary ->
                val info = rprsBillingInfoMap[summary.custCd]
                val billingKey = if (info?.rprsAcctBillCombPublYn == true) info.rprsCustCd else summary.custCd
                Triple(billingKey, summary.tstReqDivCd, summary.crcyCd)
            }
            .map { (key, summaries) ->
                ReqServiceUnbilledDemandSummary(
                    custCd = key.first,
                    custNm = null,
                    branchNm = null,
                    stndPrice = summaries.sumOf { it.stndPrice },
                    supval = summaries.sumOf { it.supval },
                    addtax = summaries.sumOf { it.addtax },
                    demandCharge = summaries.sumOf { it.demandCharge },
                    requestCount = summaries.sumOf { it.requestCount },
                    tstReqDivCd = key.second,
                    crcyCd = key.third
                )
            }

        // Batch query custNm from scs_cust_mst by billing keys
        val billingKeys = billPublSummaries.map { it.custCd }.distinct()
        val custNmMap = custCustomRepository.findCustNmMapByCustCds(billingKeys)

        billPublSummaries.forEach { summary ->
            val custBillingInfo = custNmMap[summary.custCd]
            emit(summary.toDemandResponse(
                searchStartDt = searchParam.startDt,
                searchEndDt = searchParam.endDt,
                custNm = custBillingInfo?.custNm ?: "",
                invcRecpEmailYn = custBillingInfo?.invcRecpEmailYn ?: false,
                invcRecpEmailAddr = custBillingInfo?.invcRecpEmailAddr ?: "",
                bzoffiCd = custBillingInfo?.bzoffiCd,
                sapCustCd = custBillingInfo?.sapCustCd
            ))
        }
    }

    /**
     * Get billing request details (의뢰내역 조회)
     *
     * Business Rules:
     * 1. Call req-service API to get individual test item records
     * 2. Batch query custNm from scs_cust_mst by distinct custCd list
     * 3. Map custCd to directAcctCd for req-service API call
     * 4. Return individual rows (non-aggregated) with custNm
     */
    override fun getBillingRequests(
        searchParam: BillingRequestSearchParam
    ): Flow<BillingRequestResponse> = flow {
        // bill_publ_yn=false인 거래처는 전체 제외
        val noBillPublCustCds = custCustomRepository.findNoBillPublCustCds()
        if (searchParam.custCd in noBillPublCustCds) return@flow

        // custCd를 directAcctCd로 맵핑하여 req-service 호출
        val details = reqServicePort.getBillingRequests(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            directAcctCd = searchParam.custCd,  // custCd → directAcctCd 맵핑
            closingCd = searchParam.closingCd,
            tstReqDivCd = searchParam.tstReqDivCd,
            crcyCd = searchParam.crcyCd,
        ).toList()

        // Filter out individual details for bill_publ_yn=false customers
        val filteredDetails = if (noBillPublCustCds.isNotEmpty()) {
            details.filter { it.custCd !in noBillPublCustCds }
        } else {
            details
        }

        // Batch query custNm from scs_cust_mst
        val custCds = filteredDetails.mapNotNull { it.custCd }.distinct()
        val custNmMap = custCustomRepository.findCustNmMapByCustCds(custCds)

        filteredDetails.forEach { detail ->
            val custBillingInfo = custNmMap[detail.custCd]
            emit(BillingRequestResponse(
                tstItemId = detail.tstItemId,
                tstReqDt = detail.tstReqDt,
                tstReqNo = detail.tstReqNo.toString(),
                tstReqDivCd = detail.tstReqDivCd ?: "",
                custCd = detail.custCd,
                custNm = custBillingInfo?.custNm ?: "",
                patNm = detail.patNm,
                hospChartNo = detail.hospChartNo,
                tstMediumCateCd = detail.tstMediumCateCd,
                tstCd = detail.tstCd,
                tstNm = detail.tstNm,
                stndPrice = detail.stndPrice,
                supval = detail.supval,
                addTax = detail.addTax,
                demandCharge = detail.demandCharge,
                crcyCd =  detail.crcyCd,
                exrtPrice = detail.exrtPrice,
                closingCd = detail.closingCd,
                closingSupval = detail.closingSupval,
                closingAddtax = detail.closingAddtax,
                closingSpecialCharge = detail.closingSpecialCharge,
                discountRate = detail.discountRate,
                creator = detail.creator,
                createDtime = detail.createDtime,
                closingMemo = detail.closingMemo
            ))
        }
    }
}
