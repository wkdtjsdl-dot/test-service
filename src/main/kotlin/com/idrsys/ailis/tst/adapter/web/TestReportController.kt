package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestReportUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.reactor.mono
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * 검사결과 보고서 Controller
 */
@Tag(name = "Test Result", description = "검사결과 보고서 API")
@RestController
@RequestMapping("/api/bts/tst-report")
class TestReportController(
    private val testReportUseCase: TestReportUseCase
) {

    @Operation(summary = "검사결과 목록 조회")
    @GetMapping
    fun searchTestResults(
        @ParameterObject searchParam: TestResultSearchParam
    ): Mono<ResponseEntity<List<TestResultResponse>>> {
        return mono {
            val results = testReportUseCase.searchTestResults(searchParam)
            ResponseEntity.ok(results)
        }
    }

    @Operation(summary = "검사결과 상세 조회")
    @GetMapping("/{reportId}")
    fun getTestReport(@PathVariable reportId: String): Mono<TestResultResponse> {
        return mono {
            testReportUseCase.getTestReport(reportId)
        }
    }

    @Operation(summary = "검사결과 보고서 등록")
    @PostMapping
    fun registerTestReport(
        @RequestBody request: TestReportRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<ResponseEntity<Map<String, String>>> {
        return mono {
            val reportId = testReportUseCase.registerTestReport(request, auth.adminId)
            ResponseEntity.ok(mapOf("tstReportId" to reportId))
        }
    }

    @Operation(summary = "검사결과 보고서 수정")
    @PutMapping("/{reportId}")
    fun updateTestReport(
        @PathVariable reportId: String,
        @RequestBody request: TestReportUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestResultResponse> {
        return mono {
            testReportUseCase.updateTestReport(reportId, request, auth.adminId)
        }
    }

    @Operation(summary = "보고서 파일 다운로드")
    @GetMapping("/{reportId}/file")
    fun downloadReportFile(
        @PathVariable reportId: String
    ): Mono<ResponseEntity<Resource>> {
        return mono {
            val resource = testReportUseCase.getReportFile(reportId)

            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"${resource.filename}\""
                )
                .body(resource)
        }
    }

    @Operation(summary = "보고서 배포 처리")
    @PostMapping("/{reportId}/deliver")
    fun deliverReport(
        @PathVariable reportId: String,
        @RequestBody request: DeliveryRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DeliveryResult> {
        return mono {
            testReportUseCase.deliverReport(reportId, request.deliveryMethod, auth.adminId)
        }
    }

    @Operation(summary = "검사결과 보고서 삭제")
    @DeleteMapping("/{reportId}")
    fun deleteTestReport(
        @PathVariable reportId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> {
        return mono {
            testReportUseCase.deleteTestReport(reportId, auth.adminId)
        }.then()
    }
}