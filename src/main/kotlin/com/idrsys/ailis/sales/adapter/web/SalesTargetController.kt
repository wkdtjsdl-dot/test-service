package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSaveRequest
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalesTargetDetailResponse
import com.idrsys.ailis.sales.application.dto.response.SalesTargetResponse
import com.idrsys.ailis.sales.application.usecase.salesTarget.SalesTargetUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.reactive.excel.ReactiveExcelWriter
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/sales/targets")
@Tag(name = "SalesTargetController", description = "매출목표 관리 Controller")
class SalesTargetController(
    private val salesTargetUseCase: SalesTargetUseCase,
    private val excelWriter: ReactiveExcelWriter,
) {

    @GetMapping
    @Operation(summary = "getSalesTargets", description = "매출목표 조회 - 년도별 고객별 salesTeamCd별 집계")
    suspend fun getSalesTargets(
        @ParameterObject request: SalesTargetSearchParam,
    ): ContentWrapper<SalesTargetResponse> {
        val result = salesTargetUseCase.getSalesTargets(request)
        return ContentWrapper(result)
    }

    @GetMapping("/detail")
    @Operation(summary = "getSalesTargetDetails", description = "매출목표 상세 조회 - custCd별 년월별 salesTeamCd별 집계")
    suspend fun getSalesTargetDetails(
        @RequestParam year: String,
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

    @GetMapping("/excel")
    @Operation(summary = "downloadSalesTargetList", description = "연 매출 목표 목록 Excel 다운로드")
    suspend fun downloadSalesTargetList(
        @ParameterObject @Parameter(hidden = true) request: SalesTargetSearchParam,
    ) : ResponseEntity<Flow<DataBuffer>> {
        val excelInfos = salesTargetUseCase.getSalesTargets(request)

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateString = today.format(formatter)
        val filename = "매출목표${dateString}.xlsx"
        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())

        val excelFlow = excelWriter.generateExcel(
            excelInfos,
            SalesTargetResponse::class,
            "매출목표"
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''${encodedFilename}")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Access-Control-Expose-Headers", "Content-Disposition")
            .body(excelFlow)

    }
}

/**
 * API 응답 Wrapper
 */
data class ContentWrapper<T>(
    val content: List<T>
)
