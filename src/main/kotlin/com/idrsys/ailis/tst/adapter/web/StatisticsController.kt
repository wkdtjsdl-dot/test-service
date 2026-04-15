package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.ChargeChangeApprovalUseCase
import com.idrsys.ailis.tst.application.usecase.ClinicalTestCodeUseCase
import com.idrsys.ailis.tst.application.usecase.PatientTatAnalysisUseCase
import com.idrsys.ailis.tst.application.usecase.WorklistItemUseCase
import com.idrsys.reactive.excel.ReactiveExcelWriter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.mono
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

@Tag(name = "Statistics", description = "통계 API")
@RestController
@RequestMapping("/api/stat")
class StatisticsController(
    private val worklistItemUseCase: WorklistItemUseCase,
    private val clinicalTestCodeUseCase: ClinicalTestCodeUseCase,
    private val chargeChangeApprovalUseCase: ChargeChangeApprovalUseCase,
    private val patientTatAnalysisUseCase: PatientTatAnalysisUseCase,
    private val excelWriter: ReactiveExcelWriter
) {

    @Operation(summary = "워크리스트 항목별 이전자료 목록 조회")
    @GetMapping("/wrklist-itm")
    fun search(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate?,
        @RequestParam(required = false) wrklistCd: String?,
        @RequestParam(required = false) tstCd: String?,
        @RequestParam(required = false) spcmCd: String?
    ): Flux<WorklistItemResponse> {
        val param = WorklistItemSearchParam(startDt, endDt, wrklistCd, tstCd, spcmCd)
        return mono {
            worklistItemUseCase.search(param)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "워크리스트 항목별 이전자료 엑셀 다운로드")
    @GetMapping("/wrklist-itm/excel")
    suspend fun downloadWorklistItemExcel(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate?,
        @RequestParam(required = false) wrklistCd: String?,
        @RequestParam(required = false) tstCd: String?,
        @RequestParam(required = false) spcmCd: String?
    ): ResponseEntity<Flow<DataBuffer>> {
        val param = WorklistItemSearchParam(startDt, endDt, wrklistCd, tstCd, spcmCd)
        val data = worklistItemUseCase.searchForExcel(param)
        val filename = "워크리스트항목별조회_${now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))}.xlsx"
        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
        val excelFlow = excelWriter.generateExcel(data, WorklistItemResponse::class, "워크리스트항목별조회")
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''${encodedFilename}")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Access-Control-Expose-Headers", "Content-Disposition")
            .body(excelFlow)
    }

    @Operation(summary = "임상검사코드 전체항목 목록 조회")
    @GetMapping("/clinical-test-code")
    fun getClinicalTestCodes(
        @RequestParam(required = false) tstCd: String?,
        @RequestParam(required = false) tstNm: String?,
        @RequestParam(required = false) partCd: String?,
        @RequestParam(required = false) tstStatCd: String?,
        @RequestParam(required = false) useYn: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate?
    ): Flux<ClinicalTestCodeResponse> {
        val param = ClinicalTestCodeSearchParam(tstCd, tstNm, partCd, tstStatCd, useYn, startDt, endDt)
        return mono {
            clinicalTestCodeUseCase.search(param)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "수가변경 승인처리 목록 조회")
    @GetMapping("/charge-change-approval")
    fun getChargeChangeApprovals(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate?,
        @RequestParam(required = false) tstCd: String?,
        @RequestParam(required = false) custCd: String?,
        @RequestParam(required = false) changeKindCd: String?
    ): Flux<ChargeChangeApprovalResponse> {
        val param = ChargeChangeApprovalSearchParam(startDt, endDt, tstCd, custCd, changeKindCd)
        return mono {
            chargeChangeApprovalUseCase.search(param)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "수진자별 TAT분석표 목록 조회")
    @GetMapping("/patient-tat")
    fun getPatientTatAnalysis(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDt: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDt: LocalDate,
        @RequestParam(required = false) tstCd: String?
    ): Flux<PatientTatAnalysisResponse> {
        val param = PatientTatAnalysisSearchParam(startDt, endDt, tstCd)
        return mono {
            patientTatAnalysisUseCase.search(param)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }
}
