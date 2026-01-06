package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Billing Query Use Case
 *
 * Handles read operations for billing domain
 */
interface BillingQueryUseCase {
    /**
     * Get demand list (settled or unsettled)
     */
    suspend fun getDemandList(searchParam: DemandSearchParam, pageable: Pageable): Page<*>

    /**
     * Get demand detail
     */
    suspend fun getDemandDetail(demandId: String): DemandResponse

    /**
     * Get unsettled demand summary (grouped by customer)
     */
    suspend fun getUnsettledDemandSummary(searchParam: DemandSearchParam, pageable: Pageable): Page<UnsettledDemandSummary>
}
