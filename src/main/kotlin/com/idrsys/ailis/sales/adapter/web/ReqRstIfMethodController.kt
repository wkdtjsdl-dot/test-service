package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import com.idrsys.ailis.sales.application.dto.response.ReqRstIfMethodResponse
import com.idrsys.ailis.sales.application.usecase.reqrstifmethod.ReqRstIfMethodUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/rst-if-method")
@Tag(name = "ReqRstIfMethodController", description = "고객 추가정보(연동정보) Controller")
class ReqRstIfMethodController(
    private val useCase: ReqRstIfMethodUseCase
) {

    @GetMapping
    @Operation(summary = "findAllByCustMstId", description = "고객ID로 연동정보 조회")
    fun findAllByCustMstId(
        @RequestParam custMstId: String
    ): Flow<ReqRstIfMethodResponse> {
        val searchParam = ReqRstIfMethodSearchParam(custMstId = custMstId)
        return useCase.findAllByCustMstId(searchParam)
    }

    @GetMapping("/{custMstId}")
    @Operation(summary = "findCurrentByCustMstId", description = "현재 유효한 연동정보 조회 (custMstId)")
    suspend fun findCurrentByCustMstId(
        @PathVariable custMstId: String
    ): ReqRstIfMethodResponse? {
        return useCase.findCurrentByCustMstId(custMstId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "save", description = "연동정보 저장")
    suspend fun save(
        @RequestBody command: ReqRstIfMethodCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): ReqRstIfMethodResponse {
        return useCase.save(command, auth.adminId)
    }

    @GetMapping("/possYn/{custMstId}")
    @Operation(summary = "getReqPossYn", description = "의뢰가능여부 조회")
    suspend fun getReqPossYn(
        @PathVariable custMstId: String
    ): Map<String, Boolean> {
        return useCase.getReqPossYn(custMstId)
    }

    @PatchMapping("/possYn/{custMstId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updateReqPossYn", description = "의뢰가능여부 수정")
    suspend fun updateReqPossYn(
        @PathVariable custMstId: String,
        @RequestBody request: Map<String, Boolean>,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Map<String, Boolean> {
        val reqPossYn = request["reqPossYn"]
            ?: throw IllegalArgumentException("reqPossYn is required")
        return useCase.updateReqPossYn(custMstId, reqPossYn, auth.adminId)
    }
}
