package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.RecalculateBillingCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementBatchCommand
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.RecalculateBillingResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResponse

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
     * Send sales statements to ERP in batch (매출전표 배치 생성)
     */
    suspend fun sendSalesStatementBatch(command: SendSalesStatementBatchCommand, adminId: String): SendSalesStatementBatchResponse

    /**
     * Recalculate billing demands (청구수가 재마감)
     * Recomputes sbl_demand based on current CLCD_Y tst-items and rprsAcctBillCombPublYn.
     * Does not touch tst-item closing state.
     */
    suspend fun recalculateBillingDemands(command: RecalculateBillingCommand, adminId: String): RecalculateBillingResponse
}
