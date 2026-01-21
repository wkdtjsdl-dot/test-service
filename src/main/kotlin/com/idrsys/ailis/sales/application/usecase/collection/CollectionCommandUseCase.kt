package com.idrsys.ailis.sales.application.usecase.collection

import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.response.CollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.DeleteCollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResponse
import com.idrsys.ailis.sales.application.dto.response.SplitCollectionResponse

/**
 * Collection Command Use Case
 *
 * Handles write operations for collection domain
 */
interface CollectionCommandUseCase {
    /**
     * Register card payment to customer
     */
    suspend fun registerCardPayment(command: RegisterCollectionCommand, adminId: String): CollectionBillResponse

    /**
     * Register bank deposit to customer
     */
    suspend fun registerBankDeposit(command: RegisterCollectionCommand, adminId: String): CollectionBillResponse

    /**
     * Register split payment (card or bank) to multiple customers
     */
    suspend fun registerSplitPayment(command: RegisterSplitPaymentCommand, adminId: String): SplitCollectionResponse

    /**
     * Delete collection bill
     */
    suspend fun deleteCollectionBill(colbillId: String, adminId: String): DeleteCollectionBillResponse

    /**
     * Send collection bills to ERP
     */
    suspend fun sendCollectionToErp(command: SendCollectionToErpCommand, adminId: String): SendCollectionResponse
}
