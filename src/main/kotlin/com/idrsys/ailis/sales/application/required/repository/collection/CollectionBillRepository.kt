package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionBill

/**
 * Collection Bill Repository (Port Interface)
 *
 * Combines basic CRUD operations and complex queries
 */
interface CollectionBillRepository {
    // Basic CRUD operations
    suspend fun save(collectionBill: CollectionBill): CollectionBill
    suspend fun findById(id: String): CollectionBill?
    suspend fun delete(collectionBill: CollectionBill)

    /**
     * Find collection bill by ID
     */
    suspend fun findCollectionBillById(colbillId: String): CollectionBill?
}
