package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.response.BillingRequestDomesticExcelRow
import com.idrsys.ailis.sales.application.dto.response.BillingRequestForeignExcelRow
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.DemandDomesticExcelRow
import com.idrsys.ailis.sales.application.dto.response.DemandForeignExcelRow
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.toDomesticExcelRow
import com.idrsys.ailis.sales.application.dto.response.toForeignExcelRow
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.external.ReqServicePort
import com.idrsys.ailis.sales.application.required.external.TstServicePort
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
    private val baseServicePort: BaseServicePort,
    private val tstServicePort: TstServicePort,
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
            CLCD.CLCD_Y -> flow {
                val crcyCdNmMap = getCrcyCdNmMap()
                demandRepository.findDemandsWithCustInfo(searchParam).collect { row ->
                    val response = DemandResponse.from(row)
                    emit(response.copy(crcyCdNm = crcyCdNmMap[response.crcyCd]))
                }
            }
            CLCD.CLCD_N -> {
                // Get unbilled demand summary from req-service API with custNm from batch query
                getUnbilledDemandSummaryFromReqService(searchParam)
            }
        }
    }

    private suspend fun getCrcyCdNmMap(): Map<String, String> {
        val sysCodes = baseServicePort.getChildrenSystemCodes(listOf("CRCY")) ?: return emptyMap()
        return sysCodes["CRCY"]?.associate { it.cd to it.cdNm } ?: emptyMap()
    }

    private suspend fun getTstReqDivCdNmMap(): Map<String, String> {
        val sysCodes = baseServicePort.getChildrenSystemCodes(listOf("RQDV")) ?: return emptyMap()
        return sysCodes["RQDV"]?.associate { it.cd to it.cdNm } ?: emptyMap()
    }

    private suspend fun getMediumCateNmMap(): Map<String, String> {
        return tstServicePort.getMediumCategories()
            ?.filter { it.tstMediumCateCd != null }
            ?.associate { it.tstMediumCateCd!! to (it.cateNm ?: it.tstMediumCateCd!!) }
            ?: emptyMap()
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
        val crcyCdNmMap = getCrcyCdNmMap()
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

        // Post-aggregate by rprs_cust_cd billing key
        val custCdList = finalSummaries.map { it.custCd }.distinct()
        val rprsBillingInfoMap = custCustomRepository.findRprsBillingInfoByCustCds(custCdList)

        val billPublSummaries = finalSummaries
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
                billPublYn = custBillingInfo?.billPublYn ?: false,
                invcRecpEmailYn = custBillingInfo?.invcRecpEmailYn ?: false,
                invcRecpEmailAddr = custBillingInfo?.invcRecpEmailAddr ?: "",
                bzoffiCd = custBillingInfo?.bzoffiCd,
                sapCustCd = custBillingInfo?.sapCustCd,
                crcyCdNm = crcyCdNmMap[summary.crcyCd],
            ))
        }
    }

    /**
     * Get billing request details (의뢰내역 조회)
     *
     * Business Rules:
     * 1. Resolve constituent custCds (representative + sub-accounts) from custCd
     * 2. Call req-service API to get individual test item records filtered by custCds
     * 3. Batch query custNm from scs_cust_mst by distinct custCd list
     * 4. Return individual rows (non-aggregated) with custNm
     */
    override fun getBillingRequests(
        searchParam: BillingRequestSearchParam
    ): Flow<BillingRequestResponse> = flow {
        val constituentCustCds = custCustomRepository.findConstituentCustCds(searchParam.custCd)
        val details = reqServicePort.getBillingRequests(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            custCds = constituentCustCds,
            closingCd = searchParam.closingCd,
            tstReqDivCd = searchParam.tstReqDivCd,
            crcyCd = searchParam.crcyCd,
        ).toList()

        // Batch query custNm from scs_cust_mst
        val custCds = details.mapNotNull { it.custCd }.distinct()
        val custNmMap = custCustomRepository.findCustNmMapByCustCds(custCds)

        details.forEach { detail ->
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

    override suspend fun getDomesticDemandsForExcel(searchParam: DemandSearchParam): List<DemandDomesticExcelRow> =
        getDemandList(searchParam).toList().map { it.toDomesticExcelRow() }

    override suspend fun getForeignDemandsForExcel(searchParam: DemandSearchParam): List<DemandForeignExcelRow> =
        getDemandList(searchParam).toList().map { it.toForeignExcelRow() }

    override suspend fun getBillingRequestsDomesticForExcel(searchParam: BillingRequestSearchParam): List<BillingRequestDomesticExcelRow> {
        val tstReqDivCdNmMap = getTstReqDivCdNmMap()
        val mediumCateNmMap = getMediumCateNmMap()
        return getBillingRequests(searchParam).toList().map {
            it.toDomesticExcelRow(tstReqDivCdNmMap[it.tstReqDivCd], mediumCateNmMap[it.tstMediumCateCd])
        }
    }

    override suspend fun getBillingRequestsForeignForExcel(searchParam: BillingRequestSearchParam): List<BillingRequestForeignExcelRow> {
        val crcyCdNmMap = getCrcyCdNmMap()
        val mediumCateNmMap = getMediumCateNmMap()
        return getBillingRequests(searchParam).toList().map {
            it.toForeignExcelRow(crcyCdNmMap[it.crcyCd], mediumCateNmMap[it.tstMediumCateCd])
        }
    }
}
