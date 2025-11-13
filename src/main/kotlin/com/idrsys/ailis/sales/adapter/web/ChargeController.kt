package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/charges")
@Tag(name = "ChargeController", description = "고객 수가 관리 Controller")
class ChargeController(
    private val chargeUseCase: ChargeUseCase,
) {

    @GetMapping("/list")
    @Operation(summary = "getCharges", description = "고객 수가관리 목록")
    suspend fun getCharges(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable
    ): Page<ChargeResponse> {
        return chargeUseCase.getChargePage(searchParam, pageable)
    }
}
