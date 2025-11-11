package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
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
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/custs")
@Tag(name = "CustController", description = "고객 Controller" )
class CustController(
    private val custUseCase: CustUseCase,
    private val excelWriter: ReactiveExcelWriter,
) {

    @GetMapping
    @Operation(summary = "custPage" ,description = "고객 관리 목록")
    suspend fun getCustList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable
    ): Page<CustListResponse> {
        return custUseCase.getCustPage(searchParam,pageable)
    }

    @GetMapping("/excel")
    @Operation(summary = "downloadCustList", description = "고객 관리 목록 Excel 다운로드")
    suspend fun downloadCustList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam
    ) : ResponseEntity<Flow<DataBuffer>> {
        val excelInfos = custUseCase.getCusts(searchParam)

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateString = today.format(formatter)
        val filename = "고객목록${dateString}.xlsx"
        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())

        val excelFlow = excelWriter.generateExcel(
            excelInfos,
            CustListResponse::class,
            "고객목록"
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''${encodedFilename}")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Access-Control-Expose-Headers", "Content-Disposition")
            .body(excelFlow)

    }

    @GetMapping("/{custMstId}")
    @Operation(summary = "findCustByCustMstId", description = "고객 상세 조회")
    suspend fun findCustByCustMstId(
        @PathVariable custMstId: String
    ) : CustResponse {
        return custUseCase.findCustByCustMstId(custMstId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "registerCust", description = "고객 등록")
    suspend fun registerCust(
        @RequestBody command: CustRegisterCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Cust {
        return custUseCase.registerCust(command, auth.adminId)
    }

    @PutMapping("/{custMstId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updateCust", description = "고객 수정")
    suspend fun updateCust(
        @PathVariable custMstId: String,
        @RequestBody command: CustUpdateCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) : Cust {
        return custUseCase.updateCust(custMstId, command, auth.adminId)
    }

    @GetMapping("/validation/{custCd}")
    @Operation(summary = "checkDuplicateCustCd", description = "고객 코드 중복 체크")
    suspend fun checkDuplicateCustCd(
        @PathVariable custCd: String
    ): Boolean {
        return custUseCase.isCustCdExists(custCd)
    }

    @GetMapping("/search-custCdNm")
    @Operation(summary = "getAutoCompleteCustCdNm", description = "고객코드/명 자동완성 조회")
    fun getAutoCompleteCustCdNm(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam
    ) : Flow<CustCdNmAutoCompleteResponse> {
        return custUseCase.getAutoCompleteCustCdNm(searchParam)
    }


}