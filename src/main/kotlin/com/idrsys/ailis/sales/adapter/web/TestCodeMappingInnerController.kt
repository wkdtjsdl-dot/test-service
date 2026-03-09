package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.InnerTestCodeSearchParam
import com.idrsys.ailis.sales.application.dto.response.InnerTestCodeMappingResponse
import com.idrsys.ailis.sales.application.usecase.testCodeMapping.TestCodeMappingUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/custs/tst-cd-mpgs")
@Tag(name = "TestCodeMappingInnerController", description = "고객 검사 코드 맵핑  Controller")
class TestCodeMappingInnerController(
    private val testCodeMappingUseCase: TestCodeMappingUseCase,
) {
    @PostMapping("/search")
    @Operation(summary = "getTestCodeMappingList", description = "고객 검사 코드 맵핑 목록")
    suspend fun getTestCodeMappingList(
        @RequestBody searchParam: InnerTestCodeSearchParam
    ): List<InnerTestCodeMappingResponse> {
        return testCodeMappingUseCase.innerSearchTestCodeMappingList(searchParam)
    }
}
