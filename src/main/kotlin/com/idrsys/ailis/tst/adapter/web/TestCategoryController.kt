package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.TestCategoryRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestCategoryResponse
import com.idrsys.ailis.tst.application.dto.TestCategoryUpdateRequest
import com.idrsys.ailis.tst.application.usecase.TestCategoryUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.*

@Tag(name = "Test Category", description = "검사 기준정보 검사분류 API")
@RestController
@RequestMapping("/api/bbs/tst-cate")
class TestCategoryController(
    private val testCategoryUseCase: TestCategoryUseCase
) {

    @Operation(summary = "검사분류 목록")
    @GetMapping("/{largeCateCd}")
    fun getCategoriesByLargeCategory(@PathVariable largeCateCd: String, @RequestParam(required = false) useYn: Boolean?): Flow<TestCategoryResponse> {
        return testCategoryUseCase.getCategoriesByLargeCategory(largeCateCd, useYn)
    }

    @Operation(summary = "검사분류 등록")
    @PostMapping
    suspend fun registerCategory(
        @RequestBody request: TestCategoryRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): TestCategoryResponse {
        return testCategoryUseCase.registerCategory(request, auth.adminId)
    }

    @Operation(summary = "검사분류 수정")
    @PutMapping("/{cateId}")
    suspend fun updateCategory(
        @PathVariable cateId: String,
        @RequestBody request: TestCategoryUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): TestCategoryResponse {
        return testCategoryUseCase.updateCategory(cateId, request, auth.adminId)
    }

    @Operation(summary = "검사분류 삭제")
    @DeleteMapping("/{cateId}")
    suspend fun deleteCategory(
        @PathVariable cateId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) {
        testCategoryUseCase.deleteCategory(cateId, auth.adminId)
    }
}
