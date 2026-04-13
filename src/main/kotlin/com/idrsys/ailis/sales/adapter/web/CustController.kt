package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustAtchFileUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustReqIfMethodUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse
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
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Page<CustListResponse> {
        return custUseCase.getCustPage(searchParam, pageable, auth.adminId, auth.roleCodes)
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

    @GetMapping("/{custMstId}/items")
    @Operation(summary = "findCustByCustMstId", description = "고객 의뢰 가능 검사코드 목록")
    suspend fun findTstItemByCustMstId(
        @PathVariable custMstId: String
    ) : Flow<TstServiceTstItemsResponse> {
        return custUseCase.findTstItemsByCustMstId(custMstId)
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

    @PatchMapping("/attach-file/{custMstId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "updateCustAtchFile", description = "고객 첨부파일 그룹 ID 업데이트")
    suspend fun updateCustAtchFile(
        @PathVariable custMstId: String,
        @RequestBody command: CustAtchFileUpdateCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) {
        custUseCase.updateCustAtchFile(custMstId, command, auth.adminId)
    }

    @PatchMapping("/req-if-method/{custMstId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "updateCustReqIfMethod", description = "고객 연동정보(의뢰방식/연동유형) 업데이트")
    suspend fun updateCustReqIfMethod(
        @PathVariable custMstId: String,
        @RequestBody command: CustReqIfMethodUpdateCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) {
        custUseCase.updateCustReqIfMethod(custMstId, command, auth.adminId)
    }

    @GetMapping("/simple")
    @Operation(summary = "getCustSimpleList", description = "고객 목록 조회 (SelectBox용)")
    fun getCustSimpleList(
        @RequestParam(required = false, defaultValue = "false") filterBySalesPic: Boolean,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Flow<CustCdNmAutoCompleteResponse> {
        return custUseCase.getCustSimpleList(auth.adminId, auth.roleCodes, filterBySalesPic)
    }

    @GetMapping("/autoComplete/custCdNm")
    @Operation(summary = "getCustCdNmAutoCompleteList", description = "고객코드/명 자동완성 조회")
    fun getCustCdNmAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustAutoCompleteSearchParam,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) : Flow<CustCdNmAutoCompleteResponse> {
        return custUseCase.getCustCdNmAutoCompleteList(searchParam, auth.adminId, auth.roleCodes)
    }

    @GetMapping("/autoComplete/rprsCustCdNm")
    @Operation(summary = "getRprsCustCdNmAutoCompleteList", description = "대표고객코드/명 자동완성 조회")
    fun getRprsCustCdNmAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustAutoCompleteSearchParam
    ) : Flow<RprsCustCdNmAutoCompleteResponse> {
        return custUseCase.getRprsCustCdNmAutoCompleteList(searchParam)
    }

    @GetMapping("/autoComplete/directAcctCdNm")
    @Operation(summary = "getDirectAcctCdNmAutoCompleteList", description = "직접거래처코드/명 자동완성 조회")
    fun getDirectAcctCdNmAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustAutoCompleteSearchParam
    ) : Flow<DirectAcctCdNmAutoCompleteResponse> {
        return custUseCase.getDirectAcctCdNmAutoCompleteList(searchParam)
    }

    @GetMapping("/simple-list")
    @Operation(summary = "getCustBasicList", description = "고객 기본정보 조회")
    suspend fun getCustBasicList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam
    ): List<CustBasicResponse> {
        return custUseCase.getCustList(searchParam)
    }

    @GetMapping("/{custMstId}/tst-cd-mpgs")
    @Operation(summary = "findCustTstMpgsByCustMstId", description = "고객별 검사 코드 맵핑 목록")
    suspend fun findCustTstMpgsByCustMstId(
        @PathVariable custMstId: String
    ): Flow<TestCodeMappingResponse> {
        return custUseCase.findCustTstMpgsByCustMstId(custMstId)
    }
}
