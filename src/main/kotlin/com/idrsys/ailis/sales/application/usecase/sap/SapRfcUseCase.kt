package com.idrsys.ailis.sales.application.usecase.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse

interface SapRfcUseCase {
    suspend fun callCustomerIfLabs(customers: List<CustomerIfLabsRow>): SapCustomerIfLabsResponse
    suspend fun testConnection(): String
}
