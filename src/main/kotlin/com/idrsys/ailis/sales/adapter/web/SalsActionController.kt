package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import com.idrsys.ailis.sales.application.usecase.salsAction.SalsActionUseCase
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
@RequestMapping("/api/custs/actions")
@Tag(name = "SalsActionController", description = "고객 영업활동 Controller")
class SalsActionController(
    private val salsActionUseCase: SalsActionUseCase,
) {

    @GetMapping
    @Operation(summary = "getSalsActionList", description = "고객 영업활동 정보 목록")
    suspend fun getSalsActionList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: SalsActionSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<SalsActionResponse> {
        return salsActionUseCase.getSalsActionPage(searchParam.copy(custMstId = custMstId), pageable)
    }

    @GetMapping("/{salsActionId}")
    @Operation(summary = "getSalsActionDetail", description = "고객 영업활동 정보 상세 조회")
    suspend fun getSalsActionDetail(
        @RequestParam custMstId: String,
        @PathVariable salsActionId: Long,
    ): SalsActionResponse {
        return salsActionUseCase.getSalsActionDetail(custMstId, salsActionId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createSalsAction", description = "고객 영업활동 정보 등록")
    suspend fun createSalsAction(
        @RequestParam custMstId: String,
        @RequestBody command: SalsActionCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): SalsActionResponse {
        val updatedCommand = command.copy(custMstId = custMstId)
        return salsActionUseCase.createSalsAction( updatedCommand, auth.adminId)
    }

    @PutMapping("/{salsActionId}")
    @Operation(summary = "updateSalsAction", description = "고객 영업활동 정보 수정")
    suspend fun updateSalsAction(
        @RequestParam custMstId: String,
        @PathVariable salsActionId: Long,
        @RequestBody command: SalsActionCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): SalsActionResponse {
        val updatedCommand = command.copy(custMstId = custMstId)
        return salsActionUseCase.updateSalsAction(salsActionId, updatedCommand, auth.adminId)
    }
}
