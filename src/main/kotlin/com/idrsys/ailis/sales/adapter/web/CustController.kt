package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import io.swagger.v3.oas.annotations.Parameter

@RestController
@RequestMapping("/api/custs")
@Tag(name = "CustController", description = "고객 Controller" )
class CustController(
    private val custUseCase: CustUseCase
) {

    @GetMapping
    @Operation(summary = "custPage" ,description = "고객 관리 목록")
    suspend fun getCustList(
        @ParameterObject @Parameter(hidden = true) param: CustSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable
    ): Page<CustListResponse> {
        return custUseCase.getCustPage(param,pageable)
    }
}