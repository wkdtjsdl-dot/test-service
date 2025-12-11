package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@Tag(name = "Test Item History", description = "검사 아이템 변경이력 관리 API")
@RestController
class TestItemLogsController(
    private val useCase: TestItemUseCase
) {
    @Operation(summary = "getTestItemHistoryLogList", description = "검사 검사종목 기본정보 변경이력 목록 조회")
    @GetMapping("/api/bts/item/logs")
    suspend fun getTestItemHistoryLogList(@ParameterObject @Parameter(hidden = true) searchParam: TestItemLogsSearchParam): List<TestItemLogsResponse> {
        return useCase.getTestItemHistoryLogList(searchParam)
    }
}
