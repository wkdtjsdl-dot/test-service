package com.idrsys.ailis.sales.application.required.repository.estimate

import com.idrsys.ailis.sales.domain.model.EstimateItem
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Estimate Item Repository (Data Interface)
 */
interface EstimateItemRepository : CoroutineCrudRepository<EstimateItem, String> {
    fun findByEstimateId(estimateId: String): Flow<EstimateItem>
    suspend fun deleteByEstimateId(estimateId: String)
}
