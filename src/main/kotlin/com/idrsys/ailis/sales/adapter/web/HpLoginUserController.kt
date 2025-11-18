package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import com.idrsys.ailis.sales.application.usecase.hploginuser.HpLoginUserUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/hp-login-user")
@Tag(name = "HpLoginUserController", description = "고객 추가정보(홈페이지 로그인 사용자) Controller")
class HpLoginUserController(
    private val useCase: HpLoginUserUseCase
) {

    @GetMapping
    @Operation(summary = "getHpLoginUserList", description = "홈페이지 로그인 사용자 목록")
    suspend fun getHpLoginUserList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: HpLoginUserSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<HpLoginUserResponse> {
        val updatedSearchParam = searchParam.copy(custMstId = custMstId)
        return useCase.getHpLoginUserPage(updatedSearchParam, pageable)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createHpLoginUser", description = "홈페이지 로그인 사용자 등록")
    suspend fun createHpLoginUser(
        @RequestBody command: HpLoginUserCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): HpLoginUserResponse {
        return useCase.createHpLoginUser(command.custMstId, command, auth.adminId)
    }

    @PutMapping("/{hpLoginUserId}")
    @Operation(summary = "updateHpLoginUser", description = "홈페이지 로그인 사용자 수정")
    suspend fun updateHpLoginUser(
        @PathVariable hpLoginUserId: String,
        @RequestBody command: HpLoginUserCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): HpLoginUserResponse {
        return useCase.updateHpLoginUser(hpLoginUserId, command, auth.adminId)
    }

    @DeleteMapping("/{hpLoginUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteHpLoginUser", description = "홈페이지 로그인 사용자 삭제")
    suspend fun deleteHpLoginUser(
        @PathVariable hpLoginUserId: String
    ) {
        useCase.deleteHpLoginUser(hpLoginUserId)
    }
}
