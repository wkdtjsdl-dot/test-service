package com.idrsys.ailis.sales.application.usecase.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.response.BankDepositResponse
import com.idrsys.ailis.sales.application.dto.response.CardPaymentResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerResponse
import kotlinx.coroutines.flow.Flow

/**
 * Collection Query Use Case
 *
 * Handles read operations for collection domain
 */
interface CollectionQueryUseCase {
    /**
     * Get collection ledger for customer
     */
    suspend fun getCollectionLedger(searchParam: CollectionSearchParam): CollectionLedgerResponse

    /**
     * Get card payment list (unregistered or all)
     */
    fun getCardPaymentList(searchParam: CardPaymentSearchParam): Flow<CardPaymentResponse>

    /**
     * Get bank deposit list (unregistered or all)
     */
    fun getBankDepositList(searchParam: BankDepositSearchParam): Flow<BankDepositResponse>
}
