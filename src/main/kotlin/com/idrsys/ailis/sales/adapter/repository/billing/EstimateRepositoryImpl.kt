package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateRepository
import com.idrsys.ailis.sales.domain.model.Estimate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jooq.DSLContext
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class EstimateRepositoryImpl(
    private val estimateDataRepository: EstimateDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : EstimateRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(estimate: Estimate): Estimate {
        return estimateDataRepository.save(estimate)
    }

    override suspend fun findById(id: String): Estimate? {
        return estimateDataRepository.findById(id)
    }

    override suspend fun delete(estimate: Estimate) {
        estimateDataRepository.delete(estimate)
    }

    // Custom query operations (implemented with jOOQ)
    override fun findEstimates(searchParam: EstimateSearchParam, pageable: Pageable): Flow<Estimate> {
        // TODO: Implement with jOOQ when needed
        return emptyFlow()
    }

    override suspend fun countEstimates(searchParam: EstimateSearchParam): Long {
        // TODO: Implement with jOOQ when needed
        return 0L
    }

    override suspend fun findEstimateById(estimateId: String): Estimate? {
        return estimateDataRepository.findById(estimateId)
    }

    override suspend fun generateDocNo(year: Int, docType: String): String {
        // TODO: Implement with jOOQ when needed
        return ""
    }
}