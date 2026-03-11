package com.idrsys.ailis.sales.application.dto.request.billing

/**
 * Send Sales Statement Command
 *
 * Command to send sales statement to ERP
 */
data class SendSalesStatementCommand(
    val demandId: String
)
