package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.cust.AuthKeyValidateRequest
import com.idrsys.ailis.sales.application.dto.response.AuthKeyValidateResponse
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/custs/auth-keys")
@Tag(name = "CustAuthKeyInnerController", description = "거래처 인증키 Inner Controller")
class CustAuthKeyInnerController(
    private val custUseCase: CustUseCase
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/validate")
    @Operation(summary = "validateAuthKey", description = "외부 인증키 검증")
    suspend fun validateAuthKey(
        @RequestBody request: AuthKeyValidateRequest
    ): AuthKeyValidateResponse? {
        log.info("Inner API - validateAuthKey: key=${request.extnAuthKey.take(8)}***")
        return custUseCase.validateAuthKey(request.extnAuthKey)
    }
}
