package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoCommand
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfCustInfoResponse
import com.idrsys.ailis.sales.application.usecase.ifCustInfo.IfCustInfoUseCase
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
@RequestMapping("/api/custs/excel-configs")
@Tag(name = "IfCustInfoController", description = "고객 Excel 설정 Controller")
class IfCustInfoController(
    private val ifCustInfoUseCase: IfCustInfoUseCase,
) {

    @GetMapping
    @Operation(summary = "getIfCustInfoList", description = "고객 Excel 설정 목록 조회")
    suspend fun getIfCustInfoList(
        @ParameterObject @Parameter(hidden = true) searchParam: IfCustInfoSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<IfCustInfoResponse> {
        return ifCustInfoUseCase.getIfCustInfoPage(searchParam, pageable)
    }

    @GetMapping("/{ifCustInfoId}")
    @Operation(summary = "getIfCustInfoDetail", description = "고객 Excel 설정 상세 조회")
    suspend fun getIfCustInfoDetail(
        @PathVariable ifCustInfoId: String,
    ): IfCustInfoResponse {
        return ifCustInfoUseCase.getIfCustInfoDetail(ifCustInfoId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createIfCustInfo", description = "고객 Excel 설정 등록")
    suspend fun createIfCustInfo(
        @RequestBody command: IfCustInfoCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): IfCustInfoResponse {
        return ifCustInfoUseCase.createIfCustInfo(command, auth.adminId)
    }

    @PutMapping("/{ifCustInfoId}")
    @Operation(summary = "updateIfCustInfo", description = "고객 Excel 설정 수정")
    suspend fun updateIfCustInfo(
        @PathVariable ifCustInfoId: String,
        @RequestBody command: IfCustInfoCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): IfCustInfoResponse {
        return ifCustInfoUseCase.updateIfCustInfo(ifCustInfoId, command, auth.adminId)
    }

    @DeleteMapping("/{ifCustInfoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteIfCustInfo", description = "고객 Excel 설정 삭제")
    suspend fun deleteIfCustInfo(
        @PathVariable ifCustInfoId: String,
    ) {
        ifCustInfoUseCase.deleteIfCustInfo(ifCustInfoId)
    }
}
