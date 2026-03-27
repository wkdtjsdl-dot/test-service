package com.idrsys.ailis.sales.application.service.sap

import com.idrsys.ailis.sales.adapter.external.sap.SapRfcClient
import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import com.idrsys.ailis.sales.application.usecase.sap.SapRfcUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class SapRfcService(
    private val sapRfcClient: SapRfcClient
) : SapRfcUseCase {
    override suspend fun callCustomerIfLabs(customers: List<CustomerIfLabsRow>): SapCustomerIfLabsResponse {
        // RFC calls can be blocking, so it's good practice to run them on a dedicated dispatcher
        return withContext(Dispatchers.IO) {
            sapRfcClient.executeCustomerIfLabs(customers)
        }
    }

    override suspend fun testConnection(): String {
        return withContext(Dispatchers.IO) {
            sapRfcClient.testConnection()
        }
    }
}
