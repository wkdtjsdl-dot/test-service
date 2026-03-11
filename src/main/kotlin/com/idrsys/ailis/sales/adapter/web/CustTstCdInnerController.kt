package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.ValidateTstMappingRequest
import com.idrsys.ailis.sales.application.dto.response.ValidateTstMappingResponse
import com.idrsys.ailis.sales.application.dto.response.inner.CustTstCdInnerResponse
import com.idrsys.ailis.sales.application.usecase.testCodeMapping.TestCodeMappingUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/cust-tst-cds")
@Tag(name = "CustTstCdInnerController", description = "고객검사코드 매핑 Inner Controller (서비스 간 호출용)")
class CustTstCdInnerController(
    private val testCodeMappingUseCase: TestCodeMappingUseCase
) {

    @GetMapping
    @Operation(
        summary = "getTstCd",
        description = "고객코드 + 고객검사코드로 내부 검사코드 조회"
    )
    suspend fun getTstCd(
        @RequestParam custCd: String,
        @RequestParam custTstCd: String
    ): CustTstCdInnerResponse {
        val tstCd = testCodeMappingUseCase.getTstCdByCustCdAndCustTstCd(custCd, custTstCd)
        return CustTstCdInnerResponse(
            custCd = custCd,
            custTstCd = custTstCd,
            tstCd = tstCd
        )
    }

    @PostMapping("/validate")
    @Operation(
        summary = "validateCustTstMappings",
        description = "고객검사코드 매핑 일괄 유효성 검사 (엑셀 업로드 시 사용)"
    )
    suspend fun validateCustTstMappings(
        @RequestBody request: ValidateTstMappingRequest
    ): ValidateTstMappingResponse {
        return testCodeMappingUseCase.validateCustTstMappings(request)
    }
}
