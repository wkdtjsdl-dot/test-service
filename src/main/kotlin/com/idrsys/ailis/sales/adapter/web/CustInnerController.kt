package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustBasicResponse
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/custs")
@Tag(name = "CustController", description = "고객 Controller" )
class CustInnerController(
    private val custUseCase: CustUseCase,
) {
    @GetMapping
    @Operation(summary = "getCustBasicList", description = "내부용 고객 기본정보 조회")
    suspend fun getCustBasicList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam
    ): List<CustBasicResponse> {
        return custUseCase.getCustList(searchParam)
    }
}
