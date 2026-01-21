package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementCommand
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementResponse

/**
 * Billing Command Use Case
 *
 * Handles write operations for billing domain
 */
interface BillingCommandUseCase {
    /**
     * Create demand (청구서 생성 마감)
     */
    suspend fun createDemand(command: CreateDemandCommand, adminId: String): CreateDemandResponse

    /**
     * Cancel demand (청구서 취소)
     */
    suspend fun cancelDemand(demandId: String, adminId: String): CancelDemandResponse

    /**
     * Send sales statement to ERP (매출전표 생성)
     */
    suspend fun sendSalesStatement(command: SendSalesStatementCommand, adminId: String): SendSalesStatementResponse
}
