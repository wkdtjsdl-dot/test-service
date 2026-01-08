package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionLedger
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * CollectionLedgerDataRepository
 *
 * R2DBC reactive repository for CollectionLedger entity
 */
@Repository
interface CollectionLedgerDataRepository : CoroutineCrudRepository<CollectionLedger, String> {

    /**
     * Find ledger entries by customer code
     */
    fun findByCustCd(custCd: String): Flow<CollectionLedger>

    /**
     * Find ledger entries by customer code and date range
     */
    fun findByCustCdAndColbillDtBetween(
        custCd: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<CollectionLedger>

    /**
     * Find ledger entries by division code (0: demand, 1: collection)
     */
    fun findByColbillDivCd(divisionCode: String): Flow<CollectionLedger>
}