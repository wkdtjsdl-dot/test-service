package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import com.idrsys.ailis.sales.application.usecase.chargeapprove.ChargeApproveUseCase
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

/**
 * 고객수가 승인 Controller
 */
@RestController
@RequestMapping("/api/charges/approval")
@Tag(name = "ChargeApproveController", description = "고객수가 승인 Controller")
class ChargeApproveController(
    private val chargeApproveUseCase: ChargeApproveUseCase
) {

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "requestApproval", description = "고객수가 승인 요청")
    suspend fun requestApproval(
        @RequestBody command: ChargeApproveRequestCommand,
        @JwtAuthorization auth: AuthenticationAdmin
    ): ChargeApproveResponse {
        return chargeApproveUseCase.requestApproval(command, auth.adminId)
    }

    @PostMapping("/approve")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "approve", description = "고객수가 승인")
    suspend fun approve(
        @RequestBody command: ChargeApproveActionCommand,
        @JwtAuthorization auth: AuthenticationAdmin
    ): ChargeApproveResponse {
        return chargeApproveUseCase.approve(command, auth.adminId)
    }

    @DeleteMapping("/{custChargeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteCharge", description = "고객수가 삭제 (임시저장) 또는 반려 (결재중)")
    suspend fun deleteCharge(
        @PathVariable custChargeId: String,
        @JwtAuthorization auth: AuthenticationAdmin
    ) {
        chargeApproveUseCase.deleteCharge(custChargeId, auth.adminId)
    }

    @GetMapping
    @Operation(summary = "getApprovalPage", description = "고객수가 승인 목록 조회 (페이징)")
    suspend fun getApprovalPage(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeApproveSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
        @JwtAuthorization auth: AuthenticationAdmin
    ): Page<ChargeApproveResponse> {
        return chargeApproveUseCase.getApprovalPage(searchParam, auth.adminId, pageable)
    }

    @GetMapping("/list")
    @Operation(summary = "getApprovals", description = "고객수가 승인 목록 조회 (전체)")
    suspend fun getApprovals(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeApproveSearchParam,
        @JwtAuthorization auth: AuthenticationAdmin
    ): List<ChargeApproveResponse> {
        return chargeApproveUseCase.getApprovals(searchParam, auth.adminId)
    }

    @GetMapping("/{custChargeId}")
    @Operation(summary = "getApprovalDetail", description = "고객수가 승인 상세 조회")
    suspend fun getApprovalDetail(
        @PathVariable custChargeId: String,
        @JwtAuthorization auth: AuthenticationAdmin
    ): ChargeApproveResponse {
        return chargeApproveUseCase.getApprovalDetail(custChargeId, auth.adminId)
    }
}
