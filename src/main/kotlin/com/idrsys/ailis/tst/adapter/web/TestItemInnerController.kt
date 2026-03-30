package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class TestItemInnerController(
    private val testItemUseCase: TestItemUseCase,
    private val testReferenceUseCase: TestReferenceUseCase
) {

    @Operation(summary = "검사 종목 inner 조회")
    @GetMapping("/api/inner/bts/item")
    suspend fun findTestItemByTestCode(
        @ParameterObject cds: String
    ): Flow<TestItemSimpleResponse> {
        val tstCds = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.findSimpleItemByTstCd(tstCds)
    }

    @Operation(summary = "검사 종목 inner 전체 조회")
    @GetMapping("/api/inner/bts/item/all")
    suspend fun findTestItemAll(
        @RequestParam useYn: Boolean? = null,
        @RequestParam reqPossYn: Boolean? = null
    ): Flow<TestItemSimpleResponse> {
        return testItemUseCase.findSimpleItemAll(TestItemAllSearchParam(useYn, reqPossYn))
    }

    @Operation(summary = "검사 코드별 검체 조회")
    @GetMapping("/api/inner/bts/item/specimens")
    suspend fun getSpecimensByTstCds(
        @ParameterObject cds: String
    ): Flow<TestItemSpecimensResponse> {
        val codes = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.getSpecimensByTstCds(codes)
    }

    @Operation(summary = "검사 코드별 참조항목 조회")
    @GetMapping("/api/inner/bts/item/ref-items")
    suspend fun getRefItemsByTstCds(
        @ParameterObject cds: String
    ): Flow<TestItemRefItemsResponse> {
        val codes = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.getRefItemsByTstCds(codes)
    }

    @Operation(summary = "검사 코드별 참조항목 상세 조회")
    @GetMapping("/api/inner/bts/item/ref-items/detail")
    suspend fun getRefItemsDetailByTstCds(
        @ParameterObject cds: String
    ): Flow<TestItemRefDetailItemsResponse> {
        val codes = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testItemUseCase.getRefItemsDetailByTstCds(codes)
    }

    @Operation(summary = "검사 최저수가 inner 조회")
    @GetMapping("/api/inner/bts/item/stnd-charge")
    suspend fun getStandardCharges(
        @RequestParam tstCd: String
    ): StandardChargeResponse? {
        return testItemUseCase.getCurrentStandardChargeByTest(tstCd)
    }

    @Operation(summary = "검사 검사종목 기본정보 조회")
    @GetMapping("/api/inner/bts/item/base/{tstCd}")
    fun getItem(@PathVariable tstCd: String): Mono<TestItemResponse> = mono {
        testItemUseCase.getItem(tstCd)
    }

    @Operation(summary = "검사 검사종목 참조항목 목록")
    @GetMapping("/api/inner/bts/ref-item")
    fun getRefItemsByTstCd(@ParameterObject searchParam: TestItemRefRequest): Flow<TestItemRefResponse> =
        testItemUseCase.getRefItemsByTstCd(searchParam)

    @Operation(summary = "검사 검사종목 검체 목록")
    @GetMapping("/api/inner/bts/spcm")
    fun getSpecimensByTest(@RequestParam tstCd: String): Flow<TestItemSpecimenListResponse> =
        testItemUseCase.getSpecimensByTest(tstCd)

    @Operation(summary = "참조항목 validation 정보 조회 (refCd 기준)")
    @GetMapping("/api/inner/bts/ref")
    suspend fun getRefValidationInfo(
        @RequestParam refCds: String
    ): Flow<RefValidationInfo> {
        val codes = refCds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testReferenceUseCase.getRefValidationInfoByRefCds(codes)
    }
}