package com.idrsys.ailis.sales.application.usecase.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateClosingRequest
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateCollectionCommand
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.DeleteCollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResponse
import com.idrsys.ailis.sales.application.dto.response.SplitCollectionResponse
import com.idrsys.ailis.sales.domain.model.CollectionBill
import kotlinx.coroutines.flow.Flow

/**
 * Collection Command Use Case
 *
 * Handles write operations for collection domain
 */
interface CollectionCommandUseCase {

    suspend fun findCollectionBills(searcParam: CollectionListSearchParam): Flow<CollectionBillListResponse>
    /**
     * Register card payment to customer
     */
    suspend fun registerCardPayment(command: RegisterCollectionCommand, adminId: String): CollectionBillResponse



    suspend fun updateCardPayment(colbillId:String, request: UpdateCollectionCommand, adminId: String): CollectionBillResponse

    /**
     * Register bank deposit to customer
     */
    suspend fun registerBankDeposit(command: RegisterCollectionCommand, adminId: String): CollectionBillResponse

    suspend fun updateBankDeposit(colbillId: String,request: UpdateCollectionCommand, adminId: String): CollectionBillResponse


    suspend fun registerCashOrBillPayment(command: RegisterCollectionCommand, adminId: String): CollectionBillResponse

    suspend fun updateCashOrBillPayment(colbilId:String, command: UpdateCollectionCommand, adminId: String): CollectionBillResponse

    suspend fun setColbillClosing(collBillId: String, closingCd: UpdateClosingRequest, adminId: String):CollectionBillResponse

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
