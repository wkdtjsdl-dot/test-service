package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.adapter.external.ReqServiceClient
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandType
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.usecase.billing.BillingQueryUseCase
import com.idrsys.ailis.sales.shared.mapper.toDemandResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
    private val reqServiceClient: ReqServiceClient
) : BillingQueryUseCase {

    /**
     * Get demand list (settled or unsettled)
     *
     * Business Rules:
     * 1. SETTLED: Demands that have been created (청구 완료) - from sales-service DB
     * 2. UNSETTLED: Test requests not yet included in demands (미청구) - from req-service API
     */
    override suspend fun getDemandList(searchParam: DemandSearchParam, pageable: Pageable): Page<DemandResponse> {
        return when (searchParam.demandType) {
            DemandType.SETTLED -> {
                // Get created demands from sales-service DB
                val total = demandRepository.countDemands(searchParam)
                if (total == 0L) return PageImpl(emptyList(), pageable, 0)

                val demands = demandRepository.findDemands(searchParam, pageable)
                    .map { DemandResponse.from(it) }
                    .toList()
                PageImpl(demands, pageable, total)
            }
            DemandType.UNSETTLED -> {
                // Get unbilled demand summary from req-service API
                getUnbilledDemandSummaryFromReqService(searchParam, pageable)
            }
        }
    }

    /**
     * Get demand detail
     */
    override suspend fun getDemandDetail(demandId: String): DemandResponse? {
        return demandRepository.findById(demandId)?.let { DemandResponse.from(it) }
    }

    /**
     * Get unbilled demand summary from req-service
     *
     * Business Rules:
     * 1. Call req-service API to get unbilled test items (closingCd = "CLCD_N")
     * 2. Map response to DemandResponse format
     * 3. Return empty page if req-service call fails
     */
    private suspend fun getUnbilledDemandSummaryFromReqService(
        searchParam: DemandSearchParam,
        pageable: Pageable
    ): Page<DemandResponse> {
        val reqServicePage = reqServiceClient.getUnbilledDemandSummary(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            custCd = searchParam.custCd,
            page = pageable.pageNumber,
            size = pageable.pageSize
        ) ?: return PageImpl(emptyList(), pageable, 0)

        val demandResponses = reqServicePage.content.map {
            it.toDemandResponse(searchParam.startDt)
        }

        return PageImpl(
            demandResponses,
            pageable,
            reqServicePage.totalElements
        )
    }
}
