package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtSearchParam
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.ExrtListResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtResponse
import com.idrsys.ailis.sales.application.usecase.exrt.ExrtUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/custs/exrt")
@Tag(name = "ExrtController", description = "환율 Controller")
class ExrtController(
    private val exrtUseCase: ExrtUseCase,
) {

    @GetMapping(params = ["page"])
    @Operation(summary = "getExrtList", description = "환율 목록 조회 (페이징)")
    suspend fun getExrtList(
        @ParameterObject @Parameter(hidden = true) searchParam: ExrtSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<ExrtListResponse> {
        return exrtUseCase.getExrtPage(searchParam, pageable)
    }

    @GetMapping
    @Operation(summary = "getExrtsBy", description = "환율 목록 조건 조회")
    suspend fun getExrtsBy(
        @RequestParam stndDt: String?,
        @RequestParam crcyCd: String?
    ): List<ExrtResponse> {
        val stndLocalDate = stndDt?.let { LocalDate.parse(it) }
        return exrtUseCase.getExrtsBy(stndLocalDate, crcyCd)
    }

    @GetMapping("/{exrtId}")
    @Operation(summary = "getExrtDetail", description = "환율 상세 조회")
    suspend fun getExrtDetail(
        @PathVariable exrtId: Long,
    ): ExrtResponse {
        return exrtUseCase.getExrtDetail(exrtId)
    }

    @PostMapping
    @Operation(summary = "createExrt", description = "환율 등록")
    suspend fun createExrt(
        @RequestBody command: ExrtCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): ExrtResponse {
        return exrtUseCase.createExrt(command, auth.adminId)
    }

    @PutMapping("/{exrtId}")
    @Operation(summary = "updateExrt", description = "환율 수정")
    suspend fun updateExrt(
        @PathVariable exrtId: Long,
        @RequestBody command: ExrtUpdateCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): ExrtResponse {
        return exrtUseCase.updateExrt(exrtId, command, auth.adminId)
    }

    @DeleteMapping("/{exrtId}")
    @Operation(summary = "deleteExrt", description = "환율 삭제")
    suspend fun deleteExrt(
        @PathVariable exrtId: Long,
    ) {
        exrtUseCase.deleteExrt(exrtId)
    }
}
