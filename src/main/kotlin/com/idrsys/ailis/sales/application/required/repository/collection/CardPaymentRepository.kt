package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.domain.model.CardPayment
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * Card Payment Repository (Port Interface)
 *
 * Combines basic CRUD operations and complex queries
 */
interface CardPaymentRepository {
    // Basic CRUD operations
    suspend fun save(cardPayment: CardPayment): CardPayment
    suspend fun findById(id: String): CardPayment?
    suspend fun delete(cardPayment: CardPayment)

    // Custom query operations
    /**
     * Find card payments with pagination
     */
    fun findCardPayments(searchParam: CardPaymentSearchParam, pageable: Pageable): Flow<CardPayment>

    /**
     * Count card payments
     */
    suspend fun countCardPayments(searchParam: CardPaymentSearchParam): Long

    /**
     * Find card payment by ID
     */
    suspend fun findCardPaymentById(cardPayId: String): CardPayment?

    /**
     * Update regYn flag
     */
    suspend fun updateRegYn(cardPayId: String, regYn: Boolean, updater: String)
}
