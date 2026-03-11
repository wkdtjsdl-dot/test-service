package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.Estimate
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * EstimateDataRepository
 *
 * R2DBC reactive repository for Estimate entity
 */
@Repository
interface EstimateDataRepository : CoroutineCrudRepository<Estimate, String> {

    /**
     * Find estimates by document type (EST: 견적서, TRN: 거래명세서)
     */
    fun findByDocType(docType: String): Flow<Estimate>

    /**
     * Find estimates by registration date range
     */
    fun findByRegDtBetween(startDate: LocalDate, endDate: LocalDate): Flow<Estimate>

    /**
     * Find estimates by document type and registration date range
     */
    fun findByDocTypeAndRegDtBetween(
        docType: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Estimate>

    /**
     * Find estimates by receiver (contains search)
     */
    fun findByReceiverContaining(receiver: String): Flow<Estimate>

    /**
     * Find estimates by reference (contains search)
     */
    fun findByReferenceContaining(reference: String): Flow<Estimate>
}
