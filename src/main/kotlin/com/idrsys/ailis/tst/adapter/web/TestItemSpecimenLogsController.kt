package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.TestItemSpecimenLogsResponse
import com.idrsys.ailis.tst.application.dto.TestItemSpecimenLogsSearchParam
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 검사 검사종목 검체 변경이력
 */
@Tag(name = "Test Item Specimen History", description = "검사 검사종목 검체 변경이력 관리 API")
@RestController
class TestItemSpecimenLogsController(
    private val useCase: TestItemUseCase
) {
    @Operation(summary = "getTestItemSpecimenHistoryLogList", description = "검사 검사종목 검체 변경이력 목록 조회")
    @GetMapping("/api/bts/spcm/logs")
    suspend fun getTestItemSpecimenHistoryLogList(
        @ParameterObject @Parameter(hidden = true) searchParam: TestItemSpecimenLogsSearchParam
    ): List<TestItemSpecimenLogsResponse> {
        return useCase.getTestItemSpecimenHistoryLogList(searchParam)
    }
}