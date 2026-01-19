package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.response.CustBasicResponse
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.DirectAcctCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.ExcelConfigResponse
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/custs")
@Tag(name = "CustInnerController", description = "고객 Inner Controller (서비스 간 호출용)")
class CustInnerController(
    private val custUseCase: CustUseCase,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @Operation(summary = "getCustBasicList", description = "내부용 고객 기본정보 조회 (권한 기반 필터링)")
    suspend fun getCustBasicList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustSearchParam,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): List<CustBasicResponse> {
        log.info("Inner API - getCustBasicList: adminId={}, roleCodes={}", auth.adminId, auth.roleCodes)
        return custUseCase.getCustList(searchParam, auth.adminId, auth.roleCodes)
    }

    @GetMapping("/autoComplete/custCdNm")
    @Operation(summary = "getCustCdNmAutoCompleteList", description = "내부용 고객코드/명 자동완성 조회 (권한 기반 필터링)")
    fun getCustCdNmAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustAutoCompleteSearchParam,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Flow<CustCdNmAutoCompleteResponse> {
        log.info("Inner API - getCustCdNmAutoCompleteList: adminId={}, roleCodes={}", auth.adminId, auth.roleCodes)
        return custUseCase.getCustCdNmAutoCompleteList(searchParam, auth.adminId, auth.roleCodes)
    }

    @GetMapping("/autoComplete/directAcctCdNm")
    @Operation(summary = "getDirectAcctCdNmAutoCompleteList", description = "내부용 직접거래처코드/명 자동완성 조회 (권한 기반 필터링)")
    fun getDirectAcctCdNmAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustAutoCompleteSearchParam,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Flow<DirectAcctCdNmAutoCompleteResponse> {
        log.info("Inner API - getDirectAcctCdNmAutoCompleteList: adminId={}, roleCodes={}", auth.adminId, auth.roleCodes)
        return custUseCase.getDirectAcctCdNmAutoCompleteList(searchParam, auth.adminId, auth.roleCodes)
    }

    @GetMapping("/by-cust-cd/{custCd}/interface-config")
    @Operation(summary = "getInterfaceConfigByCustCd", description = "고객별 엑셀 연동 설정 조회")
    suspend fun getInterfaceConfigByCustCd(
        @PathVariable custCd: String
    ): ExcelConfigResponse {
        return custUseCase.getInterfaceConfigByCustCd(custCd)
    }

    @GetMapping("/by-cust-cd/{custCd}/excel-fields")
    @Operation(summary = "getExcelFieldsByCustCd", description = "고객별 엑셀 필드 정보 조회")
    suspend fun getExcelFieldsByCustCd(
        @PathVariable custCd: String
    ): Flow<IfFieldInfoResponse> {
        return custUseCase.getExcelFieldsByCustCd(custCd)
    }
}
