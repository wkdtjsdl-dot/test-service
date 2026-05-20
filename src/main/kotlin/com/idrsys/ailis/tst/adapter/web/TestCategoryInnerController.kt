package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.usecase.TestCategoryUseCase
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestCategoryInnerController(
    private val testCategoryUseCase: TestCategoryUseCase
) {

    @Operation(summary = "검사 중분류 카테고리 inner 조회")
    @GetMapping("/api/inner/bbs/tst-cate")
    fun getCategories(
        @RequestParam(required = false) largeCateCd: String?,
        @RequestParam(required = false) useYn: Boolean?
    ): Flow<TestCategoryResponse> {
        return testCategoryUseCase.getCategoriesByLargeCategory(largeCateCd, useYn)
    }
}
