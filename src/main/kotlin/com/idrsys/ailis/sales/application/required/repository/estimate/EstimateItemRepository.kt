package com.idrsys.ailis.sales.application.required.repository.estimate

import com.idrsys.ailis.sales.domain.model.EstimateItem
import kotlinx.coroutines.flow.Flow

/**
 * Estimate Item Repository (Unified Interface)
 */
interface EstimateItemRepository {
    // Basic CRUD operations
    suspend fun save(estimateItem: EstimateItem): EstimateItem
    suspend fun findById(id: String): EstimateItem?
    suspend fun delete(estimateItem: EstimateItem)

    // Custom query operations
    fun findByEstimateId(estimateId: String): Flow<EstimateItem>
    suspend fun deleteByEstimateId(estimateId: String)
}
