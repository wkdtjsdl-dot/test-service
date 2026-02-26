package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.EstimateItem
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * EstimateItemDataRepository
 *
 * R2DBC reactive repository for EstimateItem entity
 */
@Repository
interface EstimateItemDataRepository : CoroutineCrudRepository<EstimateItem, String> {

    /**
     * Find estimate items by estimate ID
     */
    fun findByEstimateIdOrderByCreateDtimeDesc(estimateId: String): Flow<EstimateItem>

    /**
     * Delete estimate items by estimate ID
     */
    suspend fun deleteByEstimateId(estimateId: String): Long
}
