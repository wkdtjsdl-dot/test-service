package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.domain.model.CardPayment
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * Card Payment Custom Repository (Query Interface)
 */
interface CardPaymentCustomRepository {
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
