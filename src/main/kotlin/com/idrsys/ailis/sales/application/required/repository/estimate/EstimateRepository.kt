package com.idrsys.ailis.sales.application.required.repository.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.domain.model.Estimate
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * Estimate Repository (Unified Interface)
 *
 * 기본 CRUD 및 커스텀 쿼리를 모두 포함하는 통합 리포지토리 인터페이스
 */
interface EstimateRepository {
    // Basic CRUD operations
    suspend fun save(estimate: Estimate): Estimate
    suspend fun findById(id: String): Estimate?
    suspend fun delete(estimate: Estimate)

    // Custom query operations
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
