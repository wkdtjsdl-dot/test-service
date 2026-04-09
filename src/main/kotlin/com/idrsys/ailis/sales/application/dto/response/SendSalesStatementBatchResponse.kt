package com.idrsys.ailis.sales.application.dto.response

data class SendSalesStatementBatchResponse(
    val sentCount: Int,
    val results: List<SendSalesStatementBatchResult>
)

data class SendSalesStatementBatchResult(
    val demandId: String,
    val sent: Boolean,
    val slstmtNo: String? = null,
    val message: String
)
