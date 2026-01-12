package com.idrsys.ailis.sales.application.usecase.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Estimate Query Use Case
 *
 * Handles read operations for estimate domain
 */
interface EstimateQueryUseCase {
    /**
     * Get estimate list
     */
    suspend fun getEstimateList(searchParam: EstimateSearchParam): Flow<EstimateResponse>

    /**
     * Get estimate detail
     */
    suspend fun getEstimateDetail(estimateId: String): EstimateResponse
}
