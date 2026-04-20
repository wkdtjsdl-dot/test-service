package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactPhnoRequest
import com.idrsys.ailis.sales.application.dto.response.CustContactPhnoResponse
import com.idrsys.ailis.sales.application.usecase.custContact.CustContactUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/custs/contacts")
@Tag(name = "CustContactInnerController", description = "고객 연락처 Inner Controller")
class CustContactInnerController(
    private val custContactUseCase: CustContactUseCase,
) {

    @PostMapping("/phno")
    @Operation(summary = "getPhnosByCustCds", description = "거래처 코드 목록으로 담당자명·폰번호 일괄 조회 (inner)")
    suspend fun getPhnosByCustCds(
        @RequestBody request: CustContactPhnoRequest
    ): List<CustContactPhnoResponse> {
        return custContactUseCase.getPhnosByCustCds(request.custCdList)
    }
}
