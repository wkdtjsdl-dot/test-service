package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.domain.model.CollectionBill
import kotlinx.coroutines.flow.Flow
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

    // Custom query operations
    /**
     * Find collection bills with pagination
     */
    suspend fun findCollectionBills(searchParam: CollectionListSearchParam): Flow<CollectionBillListResponse>

    /**
     * Find collection bill by ID
     */
    suspend fun findCollectionBillById(colbillId: String): CollectionBill?

    /**
     * Get next ERP sequence number from DB sequence (4-digit padded)
     */
    suspend fun nextErpSeqNo(): String

    /**
     * Find collection bills generated from a card payment
     */
    fun findByCardPayId(cardPayId: String): Flow<CollectionBillListResponse>

    /**
     * Find collection bills generated from a bank deposit
     */
    fun findByBankDepositId(bankDepositId: String): Flow<CollectionBillListResponse>
}
