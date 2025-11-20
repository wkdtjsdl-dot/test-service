package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingCommand
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingExcelValidResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingResponse
import com.idrsys.ailis.sales.application.usecase.testCodeMapping.TestCodeMappingUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/tst-cd-mpgs")
@Tag(name = "TestCodeMappingController", description = "고객 검사 코드 맵핑  Controller")
class TestCodeMappingController(
    private val testCodeMappingUseCase: TestCodeMappingUseCase,
) {

    @GetMapping
    @Operation(summary = "getTestCodeMappingList", description = "고객 검사 코드 맵핑 목록")
    suspend fun getTestCodeMappingList(
        @ParameterObject @Parameter(hidden = true) searchParam: TestCodeMappingSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<TestCodeMappingResponse> {
        return testCodeMappingUseCase.getTestCodeMappingList(searchParam, pageable)
    }

    @PostMapping
    @Operation(summary = "createTestCodeMapping", description = "고객 검사 코드 맵핑 등록")
    suspend fun createTestCodeMapping(
        @RequestBody command: TestCodeMappingCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): TestCodeMappingResponse {
        return testCodeMappingUseCase.createTestCodeMapping(command, auth.adminId)
    }

    @PostMapping("/excel-valid")
    @Operation(summary = "validExcelTestCodeMapping", description = "고객 검사코드맵핑 엑셀일괄등록 검증")
    suspend fun validTestCodeMappingByExcel(
        @RequestBody commands: List<TestCodeMappingCommand>,
    ): Flow<TestCodeMappingExcelValidResponse> {
        return testCodeMappingUseCase.validTestCodeMappingByExcel(commands)
    }

    @PostMapping("/excel-save")
    @Operation(summary = "createExcelTestCodeMapping", description = "고객 검사코드맵핑 엑셀일괄등록 저장")
    suspend fun createTestCodeMappingByExcel(
        @RequestBody commands: List<TestCodeMappingCommand>,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): Flow<TestCodeMappingResponse> {
        return testCodeMappingUseCase.createTestCodeMappingByExcel(commands, auth.adminId)
    }
}
