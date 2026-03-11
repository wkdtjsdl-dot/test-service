package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSaveRequest
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalesTargetDetailResponse
import com.idrsys.ailis.sales.application.dto.response.SalesTargetResponse
import com.idrsys.ailis.sales.application.usecase.salesTarget.SalesTargetUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sales/targets")
@Tag(name = "SalesTargetController", description = "매출목표 관리 Controller")
class SalesTargetController(
    private val salesTargetUseCase: SalesTargetUseCase,
) {

    @GetMapping
    @Operation(summary = "getSalesTargets", description = "매출목표 조회 - 년도별 고객별 salesTeamCd별 집계")
    suspend fun getSalesTargets(
        @RequestParam year: Int,
        @RequestParam(required = false) directAcctCd: String?,
    ): ContentWrapper<SalesTargetResponse> {
        val result = salesTargetUseCase.getSalesTargets(
            SalesTargetSearchParam(year = year, directAcctCd = directAcctCd)
        )
        return ContentWrapper(result)
    }

    @GetMapping("/detail")
    @Operation(summary = "getSalesTargetDetails", description = "매출목표 상세 조회 - custCd별 년월별 salesTeamCd별 집계")
    suspend fun getSalesTargetDetails(
        @RequestParam year: Int,
        @RequestParam custCd: String,
    ): ContentWrapper<SalesTargetDetailResponse> {
        val result = salesTargetUseCase.getSalesTargetDetails(
            SalesTargetDetailSearchParam(year = year, custCd = custCd)
        )
        return ContentWrapper(result)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "saveSalesTargets", description = "매출목표 저장")
    suspend fun saveSalesTargets(
        @RequestBody request: SalesTargetSaveRequest,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin,
    ): ContentWrapper<SalesTargetDetailResponse> {
        val result = salesTargetUseCase.saveSalesTargets(request, auth.adminId)
        return ContentWrapper(result)
    }
}

/**
 * API 응답 Wrapper
 */
data class ContentWrapper<T>(
    val content: List<T>
)
