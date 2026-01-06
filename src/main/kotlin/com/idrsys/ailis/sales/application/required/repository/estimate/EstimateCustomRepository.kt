package com.idrsys.ailis.sales.application.required.repository.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.domain.model.Estimate
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * Estimate Custom Repository (Query Interface)
 */
interface EstimateCustomRepository {
    /**
     * Find estimates with pagination
     */
    fun findEstimates(searchParam: EstimateSearchParam, pageable: Pageable): Flow<Estimate>

    /**
     * Count estimates
     */
    suspend fun countEstimates(searchParam: EstimateSearchParam): Long

    /**
     * Find estimate by ID
     */
    suspend fun findEstimateById(estimateId: String): Estimate?

    /**
     * Generate document number
     */
    suspend fun generateDocNo(year: Int, docType: String): String
}
