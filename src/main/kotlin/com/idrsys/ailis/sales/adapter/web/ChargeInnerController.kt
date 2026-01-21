package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/inner/custs/charges")
@Tag(name = "ChargeInnerController", description = "고객수가 Inner API Controller")
class ChargeInnerController(
    private val chargeUseCase: ChargeUseCase
) {
    @GetMapping
    @Operation(summary = "getCustChargesForBilling", description = "청구수가 재계산용 고객수가 조회")
    suspend fun getCustChargesForBilling(
        @RequestParam custCds: List<String>,
        @RequestParam tstCds: List<String>,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate
    ): List<CustChargeInnerResponse> {
        return chargeUseCase.getCustChargesForBilling(custCds, tstCds, startDt, endDt)
    }
}
