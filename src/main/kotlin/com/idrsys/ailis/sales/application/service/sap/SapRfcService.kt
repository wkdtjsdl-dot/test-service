package com.idrsys.ailis.sales.application.service.sap

import com.idrsys.ailis.sales.adapter.external.sap.SapRfcClient
import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRequest
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import com.idrsys.ailis.sales.application.usecase.sap.SapRfcUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class SapRfcService(
    private val sapRfcClient: SapRfcClient
) : SapRfcUseCase {
    override suspend fun callCustomerIfLabs(request: CustomerIfLabsRequest): SapCustomerIfLabsResponse {
        return withContext(Dispatchers.IO) {
            sapRfcClient.executeCustomerIfLabs(request)
        }
    }
}
