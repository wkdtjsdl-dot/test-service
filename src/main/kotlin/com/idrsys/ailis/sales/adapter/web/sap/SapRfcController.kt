package com.idrsys.ailis.sales.adapter.web.sap

import com.idrsys.ailis.sales.application.dto.request.sap.CustomerIfLabsRow
import com.idrsys.ailis.sales.application.dto.response.sap.SapCustomerIfLabsResponse
import com.idrsys.ailis.sales.application.usecase.sap.SapRfcUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/sap/rfc")
@Tag(name = "SAP RFC", description = "Controller for calling SAP RFC functions")
class SapRfcController(
    private val sapRfcUseCase: SapRfcUseCase
) {

    @PostMapping("/customer-if-labs")
    @Operation(summary = "Call ZFI_CUSTOMER_IF_LABS", description = "Calls the ZFI_CUSTOMER_IF_LABS RFC function in SAP.")
    suspend fun callCustomerIfLabs(@RequestBody customers: List<CustomerIfLabsRow>): SapCustomerIfLabsResponse {
        return sapRfcUseCase.callCustomerIfLabs(customers)
    }

    @GetMapping("/test-connection")
    @Operation(summary = "Test SAP Connection", description = "Pings the configured SAP destination to check connectivity.")
    suspend fun testSapConnection(): String {
        return sapRfcUseCase.testConnection()
    }
}
