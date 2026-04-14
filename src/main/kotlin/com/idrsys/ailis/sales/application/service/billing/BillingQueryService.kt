package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
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
    private val custCustomRepository: CustCustomRepository
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

        // Determine directAcctCds based on frgnAcctYn, custCd, and branchCd
        val directAcctCds: List<String>? = when {
            !searchParam.custCd.isNullOrBlank() -> listOf(searchParam.custCd)
            searchParam.frgnAcctYn == true -> custCustomRepository.findDirectAcctCdsByFrgnAcctYn(true, branchCd)
            branchCd != null -> {
                // frgnAcctYn이 null(전체) 또는 false(국내)이고 branchCd만 있는 경우
                // 해당 영업소의 국내+해외 계정 모두 조회 (frgnAcctYn 필터는 이후 in-memory에서 처리)
                custCustomRepository.findDirectAcctCdsByFrgnAcctYn(false, branchCd) +
                    custCustomRepository.findDirectAcctCdsByFrgnAcctYn(true, branchCd)
            }
            else -> null
        }

        // 영업소 필터링 결과 매칭 계정이 없으면 빈 결과 반환
        if (directAcctCds != null && directAcctCds.isEmpty()) return@flow

        val summaries = reqServicePort.getUnbilledDemandSummary(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            directAcctCds = directAcctCds
        )

        // For domestic only (frgnAcctYn=false): filter out foreign accounts
        val filteredSummaries = if (searchParam.frgnAcctYn == false) {
            val foreignAcctCds = custCustomRepository.findDirectAcctCdsByFrgnAcctYn(true).toSet()
            summaries.filter { it.directAcctCd !in foreignAcctCds }
        } else {
            summaries
        }

        // Batch query custNm from scs_cust_mst
        val custCds = filteredSummaries.map { it.directAcctCd }.distinct()
        val custNmMap = custCustomRepository.findCustNmMapByCustCds(custCds)

        filteredSummaries.forEach { summary ->
            val custBillingInfo = custNmMap[summary.directAcctCd]
            emit(summary.toDemandResponse(
                searchStartDt = searchParam.startDt,
                searchEndDt = searchParam.endDt,
                custNm = custBillingInfo?.custNm ?: "",
                invcRecpEmailYn = custBillingInfo?.invcRecpEmailYn ?: false,
                invcRecpEmailAddr = custBillingInfo?.invcRecpEmailAddr ?: "",
                bzoffiCd = custBillingInfo?.bzoffiCd,
                sapCustCd = custBillingInfo?.sapCustCd,
                crcyCd = custBillingInfo?.crcyCd
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
        // custCd를 directAcctCd로 맵핑하여 req-service 호출
        val details = reqServicePort.getBillingRequests(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            directAcctCd = searchParam.custCd,  // custCd → directAcctCd 맵핑
            closingCd = searchParam.closingCd
        ).toList()

        // Batch query custNm from scs_cust_mst
        val custCds = details.mapNotNull { it.custCd }.distinct()
        val custNmMap = custCustomRepository.findCustNmMapByCustCds(custCds)

        details.forEach { detail ->
            val custBillingInfo = custNmMap[detail.custCd]
            emit(BillingRequestResponse(
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
