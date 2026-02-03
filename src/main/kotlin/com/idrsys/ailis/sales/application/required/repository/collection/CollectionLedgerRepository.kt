package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerTransaction
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import kotlinx.coroutines.flow.Flow

/**
 * Collection Ledger Repository (Port Interface)
 */
interface CollectionLedgerRepository {
    suspend fun save(collectionLedger: CollectionLedger): CollectionLedger
    suspend fun findById(id: String): CollectionLedger?
    suspend fun delete(collectionLedger: CollectionLedger)
    fun findByCustCdOrderByColbillDtAsc(
        custCd: String,
    ): Flow<CollectionLedger>
    fun findLedgerTransactionsWithBalance(custCd: String): Flow<CollectionLedgerTransaction>
}
