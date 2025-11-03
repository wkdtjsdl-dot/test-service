package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.reactive.excel.ReactiveExcelWriter
import io.swagger.v3.oas.annotations.Parameter
import kotlinx.coroutines.flow.Flow
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
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

}