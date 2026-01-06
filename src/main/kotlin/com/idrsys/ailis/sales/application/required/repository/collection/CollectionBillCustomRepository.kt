package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.domain.model.CollectionBill
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * Collection Bill Custom Repository (Query Interface)
 */
interface CollectionBillCustomRepository {
    /**
     * Find collection bills with pagination
     */
    fun findCollectionBills(searchParam: CollectionSearchParam, pageable: Pageable): Flow<CollectionBill>

    /**
     * Count collection bills
     */
    suspend fun countCollectionBills(searchParam: CollectionSearchParam): Long

    /**
     * Find collection bill by ID
     */
    suspend fun findCollectionBillById(colbillId: String): CollectionBill?
}
