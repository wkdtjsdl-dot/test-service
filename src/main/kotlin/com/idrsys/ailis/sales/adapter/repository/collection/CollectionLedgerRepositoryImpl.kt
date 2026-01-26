package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import kotlinx.coroutines.flow.Flow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CollectionLedgerRepositoryImpl(
    private val collectionLedgerDataRepository: CollectionLedgerDataRepository,
    private val databaseClient: DatabaseClient
) : CollectionLedgerRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(collectionLedger: CollectionLedger): CollectionLedger {
        return collectionLedgerDataRepository.save(collectionLedger)
    }

    override suspend fun findById(id: String): CollectionLedger? {
        return collectionLedgerDataRepository.findById(id)
    }

    override suspend fun delete(collectionLedger: CollectionLedger) {
        collectionLedgerDataRepository.delete(collectionLedger)
    }

    override fun findByCustCdOrderByColbillDtAsc(
        custCd: String,
    ): Flow<CollectionLedger> {
        return collectionLedgerDataRepository.findByCustCd(custCd)
    }
}
