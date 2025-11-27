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
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "Test Category", description = "검사 기준정보 검사분류 API")
@RestController
@RequestMapping("/api/tst/bbs/tst-cate")
class TestCategoryController(
    private val testCategoryUseCase: TestCategoryUseCase
) {

    @Operation(summary = "검사분류 목록")
    @GetMapping("/{largeCateCd}")
    fun getCategoriesByLargeCategory(@PathVariable largeCateCd: String): Flux<TestCategoryResponse> {
        return mono {
            testCategoryUseCase.getCategoriesByLargeCategory(largeCateCd)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "검사분류 등록")
    @PostMapping
    fun registerCategory(
        @RequestBody request: TestCategoryRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestCategoryResponse> {
        return mono {
            testCategoryUseCase.registerCategory(request, auth.adminId)
        }
    }

    @Operation(summary = "검사분류 수정")
    @PutMapping("/{mediumCateCd}")
    fun updateCategory(
        @PathVariable mediumCateCd: String,
        @RequestBody request: TestCategoryUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestCategoryResponse> {
        return mono {
            testCategoryUseCase.updateCategory(mediumCateCd, request, auth.adminId)
        }
    }

    @Operation(summary = "검사분류 삭제")
    @DeleteMapping("/{mediumCateCd}")
    fun deleteCategory(
        @PathVariable mediumCateCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> {
        return mono {
            testCategoryUseCase.deleteCategory(mediumCateCd, auth.adminId)
        }.then()
    }
}
