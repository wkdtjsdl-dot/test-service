package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
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
     *
     * Returns unified DemandResponse type for both cases
     */
    suspend fun getDemandList(searchParam: DemandSearchParam, pageable: Pageable): Page<DemandResponse>

    /**
     * Get demand detail
     */
    suspend fun getDemandDetail(demandId: String): DemandResponse?
}
