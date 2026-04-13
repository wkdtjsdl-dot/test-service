package com.idrsys.ailis.sales.adapter.web.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRequest
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import com.idrsys.ailis.sales.application.usecase.sap.SapRfcUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("sap")
@RequestMapping("/api/sap/rfc")
@Tag(name = "SAP RFC", description = "Controller for calling SAP RFC functions")
class SapRfcController(
    private val sapRfcUseCase: SapRfcUseCase
) {

    @PostMapping("/customer-if-labs")
    @Operation(summary = "Call ZFI_CUSTOMER_IF_LABS", description = "Calls the ZFI_CUSTOMER_IF_LABS RFC function in SAP.")
    suspend fun callCustomerIfLabs(@RequestBody request: CustomerIfLabsRequest): SapCustomerIfLabsResponse {
        return sapRfcUseCase.callCustomerIfLabs(request)
    }
}
