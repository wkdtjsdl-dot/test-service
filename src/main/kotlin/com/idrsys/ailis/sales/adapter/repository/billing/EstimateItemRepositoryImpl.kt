package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateItemRepository
import com.idrsys.ailis.sales.domain.model.EstimateItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository

@Repository
class EstimateItemRepositoryImpl(
    private val estimateItemDataRepository: EstimateItemDataRepository,
    private val databaseClient: DatabaseClient
) : EstimateItemRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(estimateItem: EstimateItem): EstimateItem {
        return estimateItemDataRepository.save(estimateItem)
    }

    override suspend fun findById(id: String): EstimateItem? {
        return estimateItemDataRepository.findById(id)
    }

    override suspend fun delete(estimateItem: EstimateItem) {
        estimateItemDataRepository.delete(estimateItem)
    }

    // Custom query operations (delegated to DataRepository)
    override fun findByEstimateId(estimateId: String): Flow<EstimateItem> {
        return estimateItemDataRepository.findByEstimateId(estimateId)
    }

    override suspend fun deleteByEstimateId(estimateId: String) {
        estimateItemDataRepository.deleteByEstimateId(estimateId)
    }

    override suspend fun findMaxSeqByEstimateId(estimateId: String): Int? {
        return databaseClient.sql(
            """
            SELECT MAX(seq) as max_seq
            FROM sales_scm.sbl_estimate_dtl
            WHERE estimate_id = :estimateId
            """.trimIndent()
        )
            .bind("estimateId", estimateId)
            .map { row -> row.get("max_seq", Integer::class.java) }
            .awaitOneOrNull()
            ?.toInt()
    }
}