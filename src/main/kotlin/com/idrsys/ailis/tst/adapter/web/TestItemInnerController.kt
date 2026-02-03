package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.StandardChargeResponse
import com.idrsys.ailis.tst.application.dto.TestItemSimpleResponse
import com.idrsys.ailis.tst.application.dto.TestItemSpecimensResponse
import com.idrsys.ailis.tst.application.dto.TestItemRefItemsResponse
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

    @Operation(summary = "검사 종목 inner 전체 조회")
    @GetMapping("/all")
    suspend fun findTestItemAll(): Flow<TestItemSimpleResponse> {
        return testItemUseCase.findSimpleItemAll()
    }

    @Operation(summary = "검사 코드별 검체 조회")
    @GetMapping("/specimens")
    suspend fun getSpecimensByTstCds(
        @ParameterObject cds: String
    ): Flow<TestItemSpecimensResponse> {
        val codes = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.getSpecimensByTstCds(codes)
    }

    @Operation(summary = "검사 코드별 참조항목 조회")
    @GetMapping("/ref-items")
    suspend fun getRefItemsByTstCds(
        @ParameterObject cds: String
    ): Flow<TestItemRefItemsResponse> {
        val codes = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.getRefItemsByTstCds(codes)
    }

    @Operation(summary = "검사 최저수가 inner 조회")
    @GetMapping("/stnd-charge")
    suspend fun getStandardCharges(
        @RequestParam tstCd: String
    ): StandardChargeResponse? {
        return testItemUseCase.getCurrentStandardChargeByTest(tstCd)
    }
}