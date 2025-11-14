package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import com.idrsys.ailis.sales.application.usecase.hploginuser.HpLoginUserUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/hp-login-user")
@Tag(name = "HpLoginUserController", description = "고객 추가정보(홈페이지 로그인 사용자) Controller")
class HpLoginUserController(
    private val useCase: HpLoginUserUseCase
) {

    @GetMapping("/{hpLoginUserId}")
    @Operation(summary = "findById", description = "홈페이지 로그인 사용자 단건 조회")
    suspend fun findById(
        @PathVariable hpLoginUserId: String
    ): HpLoginUserResponse? {
        return useCase.findById(hpLoginUserId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "save", description = "홈페이지 로그인 사용자 저장")
    suspend fun save(
        @RequestBody command: HpLoginUserCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): HpLoginUserResponse {
        return useCase.save(command, auth.adminId)
    }
}
