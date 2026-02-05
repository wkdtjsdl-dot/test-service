package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.request.charge.ExcelChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.ExcelRegisterValidationResponse
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import jakarta.validation.Valid
import com.idrsys.reactive.excel.ReactiveExcelWriter
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@RestController
@RequestMapping("/api/custs/charges")
@Tag(name = "ChargeController", description = "고객 수가 관리 Controller")
class ChargeController(
    private val chargeUseCase: ChargeUseCase,
    private val excelWriter: ReactiveExcelWriter
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "registerCharge", description = "고객 검사코드 수가정보 등록")
    suspend fun registerCharge(
        @Valid @RequestBody command: ChargeRegisterCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): ChargeResponse {
        return chargeUseCase.registerCharge(command, auth.adminId)
    }

    @PutMapping("/{custChargeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updateCharge", description = "고객 수가 수정")
    suspend fun updateCharge(
        @PathVariable custChargeId: String,
        @RequestBody command: ChargeUpdateCommand,
        @JwtAuthorization auth: AuthenticationAdmin
    ): ChargeResponse {
        return chargeUseCase.updateCharge(custChargeId, command, auth.adminId)
    }

    @DeleteMapping("/{custChargeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteCharge", description = "고객 수가 삭제")
    suspend fun deleteCharge(
        @PathVariable custChargeId: String,
        @JwtAuthorization auth: AuthenticationAdmin
    ) {
        chargeUseCase.deleteCharge(custChargeId)
    }

    @GetMapping("/{custChargeId}")
    @Operation(summary = "getCharge", description = "고객 수가 단건 조회")
    suspend fun getCharge(
        @PathVariable custChargeId: String,
    ): ChargeResponse {
        return chargeUseCase.getCharge(custChargeId)
    }

    @GetMapping
    @Operation(summary = "getChargePage", description = "고객 수가관리 페이징 목록")
    suspend fun getChargePage(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable
    ): Page<ChargeResponse> {
        return chargeUseCase.getChargePage(searchParam, pageable, isExcel = false)
    }

    @GetMapping("/list")
    @Operation(summary = "getCharges", description = "고객 수가관리 목록")
    suspend fun getCharges(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeSearchParam
    ): List<ChargeResponse> {
        return chargeUseCase.getCharges(searchParam, isExcel = false)
    }

    @GetMapping("/excel")
    @Operation(summary = "downloadChargesExcel", description = "고객 수가 목록 엑셀 다운로드")
    suspend fun downloadChargesExcel(
        @ParameterObject @Parameter(hidden = true) searchParam: ChargeSearchParam
    ): ResponseEntity<Flow<DataBuffer>> {
        val excelInfos = chargeUseCase.getCharges(searchParam, isExcel = true)
        val today = LocalDate.now().toString()
        val filename = "고객수가목록_${today}.xlsx"
        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())

        val excelFlow = excelWriter.generateExcel(
            excelInfos,
            ChargeResponse::class,
            "고객수가목록"
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''${encodedFilename}")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Access-Control-Expose-Headers", "Content-Disposition")
            .body(excelFlow)
    }

    @PostMapping("/excel-valid")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "validateExcelRegisterCharges", description = "고객 수가 일괄 등록 사전 검증")
    suspend fun validateExcelCharges(
        @Valid @RequestBody commands: List<ExcelChargeRegisterCommand>,
    ): ExcelRegisterValidationResponse {
        return chargeUseCase.validateExcelRegisterCharges(commands)
    }

    @PostMapping("/excel-save")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "excelRegisterCharges", description = "고객 수가 일괄 등록")
    suspend fun excelRegisterCharges(
        @Valid @RequestBody commands: List<ExcelChargeRegisterCommand>,
        @JwtAuthorization auth: AuthenticationAdmin
    ): List<ChargeResponse> {
        return chargeUseCase.excelRegisterCharges(commands, auth.adminId)
    }
}
