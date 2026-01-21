package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import kotlinx.coroutines.flow.Flow

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
    fun getDemandList(searchParam: DemandSearchParam): Flow<DemandResponse>

    /**
     * Get demand detail
     */
    suspend fun getDemandDetail(demandId: String): DemandResponse?

    /**
     * Get billing request details (의뢰내역 조회)
     *
     * Returns individual test item records (non-aggregated)
     */
    fun getBillingRequests(searchParam: BillingRequestSearchParam): Flow<BillingRequestResponse>
}
