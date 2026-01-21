package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionLedger
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Collection Ledger Repository (Port Interface)
 */
interface CollectionLedgerRepository {
    suspend fun save(collectionLedger: CollectionLedger): CollectionLedger
    suspend fun findById(id: String): CollectionLedger?
    suspend fun delete(collectionLedger: CollectionLedger)
    fun findByCustCdAndColbillDtBetweenOrderByColbillDtAsc(
        custCd: String,
        startDt: LocalDate,
        endDt: LocalDate
    ): Flow<CollectionLedger>
}
