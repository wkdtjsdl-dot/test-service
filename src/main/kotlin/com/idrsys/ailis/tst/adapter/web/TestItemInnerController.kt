package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.TestItemSearchParam
import com.idrsys.ailis.tst.application.dto.TestItemSimpleResponse
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/bts/item")
class TestItemInnerController(
    private val testItemUseCase: TestItemUseCase
) {

    @Operation(summary = "검사 종목 inner 조회")
    @GetMapping
    suspend fun findTestItemByTestCode(
        @ParameterObject cds: String
    ): Flow<TestItemSimpleResponse> {
        val tstCds = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.findSimpleItemByTstCd(tstCds)
    }
}