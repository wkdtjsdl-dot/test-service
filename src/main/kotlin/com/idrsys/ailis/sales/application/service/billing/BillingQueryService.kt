package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.required.port.ReqServicePort
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.usecase.billing.BillingQueryUseCase
import com.idrsys.ailis.sales.shared.mapper.toDemandResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    private val reqServicePort: ReqServicePort
) : BillingQueryUseCase {

    /**
     * Get demand list (settled or unsettled)
     *
     * Business Rules:
     * 1. SETTLED: Demands that have been created (청구 완료) - from sales-service DB
     * 2. UNSETTLED: Test requests not yet included in demands (미청구) - from req-service API
     */
    override fun getDemandList(searchParam: DemandSearchParam): Flow<DemandResponse> {
        return when (searchParam.clcdYn) {
            CLCD.CLCD_Y -> {
                // Get created demands from sales-service DB
                demandRepository.findDemands(searchParam)
                    .map { DemandResponse.from(it) }
            }
            CLCD.CLCD_N -> {
                // Get unbilled demand summary from req-service API
                getUnbilledDemandSummaryFromReqService(searchParam)
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
     * 3. Return empty flow if req-service call fails
     */
    private fun getUnbilledDemandSummaryFromReqService(
        searchParam: DemandSearchParam
    ): Flow<DemandResponse> = flow {
        val summaries = reqServicePort.getUnbilledDemandSummary(
            startDt = searchParam.startDt,
            endDt = searchParam.endDt,
            custCd = searchParam.custCd
        )

        summaries.forEach { summary ->
            emit(summary.toDemandResponse(searchParam.startDt, searchParam.endDt))
        }
    }
}
