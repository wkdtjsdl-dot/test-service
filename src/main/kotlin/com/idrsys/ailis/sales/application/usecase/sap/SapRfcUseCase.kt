package com.idrsys.ailis.sales.application.usecase.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRequest
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse

interface SapRfcUseCase {
    suspend fun callCustomerIfLabs(request: CustomerIfLabsRequest): SapCustomerIfLabsResponse
}
