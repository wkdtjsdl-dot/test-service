package com.idrsys.ailis.sales.application.usecase.billing

import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.BillingRequestDomesticExcelRow
import com.idrsys.ailis.sales.application.dto.response.BillingRequestForeignExcelRow
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.DemandDomesticExcelRow
import com.idrsys.ailis.sales.application.dto.response.DemandForeignExcelRow
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import kotlinx.coroutines.flow.Flow

/**
 * Billing Query Use Case
 *
 * Handles read operations for billing domain
 */
interface BillingQueryUseCase {
    /**
     * Get demand list (settled or unsettled)
     *
     * Returns unified DemandResponse type for both cases
     */
    fun getDemandList(searchParam: DemandSearchParam): Flow<DemandResponse>

    /**
     * Get billing request details (의뢰내역 조회)
     *
     * Returns individual test item records (non-aggregated)
     */
    fun getBillingRequests(searchParam: BillingRequestSearchParam): Flow<BillingRequestResponse>

    suspend fun getDomesticDemandsForExcel(searchParam: DemandSearchParam): List<DemandDomesticExcelRow>

    suspend fun getForeignDemandsForExcel(searchParam: DemandSearchParam): List<DemandForeignExcelRow>

    suspend fun getBillingRequestsDomesticForExcel(searchParam: BillingRequestSearchParam): List<BillingRequestDomesticExcelRow>

    suspend fun getBillingRequestsForeignForExcel(searchParam: BillingRequestSearchParam): List<BillingRequestForeignExcelRow>
}
