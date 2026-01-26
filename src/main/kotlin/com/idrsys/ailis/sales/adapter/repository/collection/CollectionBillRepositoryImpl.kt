package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.required.repository.collection.CollectionBillRepository
import com.idrsys.ailis.sales.domain.model.CollectionBill
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jooq.DSLContext
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CollectionBillRepositoryImpl(
    private val collectionBillDataRepository: CollectionBillDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : CollectionBillRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(collectionBill: CollectionBill): CollectionBill {
        return collectionBillDataRepository.save(collectionBill)
    }

    override suspend fun findById(id: String): CollectionBill? {
        return collectionBillDataRepository.findById(id)
    }

    override suspend fun delete(collectionBill: CollectionBill) {
        collectionBillDataRepository.delete(collectionBill)
    }

    override suspend fun findCollectionBillById(colbillId: String): CollectionBill? {
        return collectionBillDataRepository.findById(colbillId)
    }
}
