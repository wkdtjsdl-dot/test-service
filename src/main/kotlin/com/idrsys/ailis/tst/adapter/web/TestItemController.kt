package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "Test Item", description = "검사 아이템 관리 API")
@RestController
class TestItemController(
    private val useCase: TestItemUseCase
) {

    // --- TestItem ---

    @Operation(summary = "검사 항목 등록")
    @PostMapping("/api/bts/item/base")
    fun registerItem(
        @RequestBody request: TestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.registerItem(request, auth.adminId)
    }

    @Operation(summary = "검사 항목 조회")
    @GetMapping("/api/bts/item/base/{id}")
    fun getItem(@PathVariable id: String): Mono<TestItemResponse> = mono {
        useCase.getItem(id)
    }

    @Operation(summary = "검사 항목 수정")
    @PutMapping("/api/bts/item/base/{id}")
    fun updateItem(
        @PathVariable id: String,
        @RequestBody request: TestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.updateItem(id, request, auth.adminId)
    }

    @Operation(summary = "검사 항목 삭제")
    @DeleteMapping("/api/bts/item/base/{id}")
    fun deleteItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteItem(id, auth.adminId)
        null
    }

    @Operation(summary = "검사 항목 전체 조회")
    @GetMapping("/api/bts/item")
    fun getAllItems(): Flow<TestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllItems().collect { emit(it) }
        }
    }

    @Operation(summary = "검사 항목 목록 조회 (대분류코드별)")
    @GetMapping("/api/bts/item/by-large-cate/{code}")
    fun getItemsByLargeCate(@PathVariable code: String): Flow<TestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getItemsByLargeCate(code).collect { emit(it) }
        }
    }

    // --- StandardCharge ---

    @Operation(summary = "표준 수가 등록")
    @PostMapping("/api/bts/item/stnd-charge")
    fun registerCharge(
        @RequestBody request: StandardChargeRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<StandardChargeResponse> = mono {
        useCase.registerCharge(request, auth.adminId)
    }

    @Operation(summary = "표준 수가 조회")
    @GetMapping("/api/bts/item/stnd-charge/{id}")
    fun getCharge(@PathVariable id: String): Mono<StandardChargeResponse> = mono {
        useCase.getCharge(id)
    }

    @Operation(summary = "표준 수가 삭제")
    @DeleteMapping("/api/bts/item/stnd-charge/{id}")
    fun deleteCharge(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteCharge(id, auth.adminId)
        null
    }

    @Operation(summary = "표준 수가 목록 조회 (검사코드별)")
    @GetMapping("/api/bts/item/stnd-charge/by-test/{tstCd}")
    fun getChargesByTest(@PathVariable tstCd: String): Flow<StandardChargeResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getChargesByTest(tstCd).collect { emit(it) }
        }
    }

    // --- TestItemSpecimen ---

    @Operation(summary = "검사 항목별 검체 등록")
    @PostMapping("/api/bts/spcm")
    fun registerSpecimen(
        @RequestBody request: TestItemSpecimenRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemSpecimenResponse> = mono {
        useCase.registerSpecimen(request, auth.adminId)
    }

    @Operation(summary = "검사 항목별 검체 조회")
    @GetMapping("/api/bts/spcm/{id}")
    fun getSpecimen(@PathVariable id: String): Mono<TestItemSpecimenResponse> = mono {
        useCase.getSpecimen(id)
    }

    @Operation(summary = "검사 항목별 검체 삭제")
    @DeleteMapping("/api/bts/spcm/{id}")
    fun deleteSpecimen(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteSpecimen(id, auth.adminId)
        null
    }

    @Operation(summary = "검사 항목별 검체 목록 조회 (검사코드별)")
    @GetMapping("/api/bts/spcm/by-test/{tstCd}")
    fun getSpecimensByTest(@PathVariable tstCd: String): Flow<TestItemSpecimenResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getSpecimensByTest(tstCd).collect { emit(it) }
        }
    }
}
