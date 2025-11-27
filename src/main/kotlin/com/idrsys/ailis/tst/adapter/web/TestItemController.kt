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
@RequestMapping("/tst/item")
class TestItemController(
    private val useCase: TestItemUseCase
) {

    // --- TestItem ---

    @PostMapping
    fun registerItem(
        @RequestBody request: TestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.registerItem(request, auth.adminId)
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: String): Mono<TestItemResponse> = mono {
        useCase.getItem(id)
    }

    @PutMapping("/{id}")
    fun updateItem(
        @PathVariable id: String,
        @RequestBody request: TestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.updateItem(id, request, auth.adminId)
    }

    @DeleteMapping("/{id}")
    fun deleteItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteItem(id, auth.adminId)
        null
    }

    @GetMapping
    fun getAllItems(): Flow<TestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllItems().collect { emit(it) }
        }
    }

    @GetMapping("/by-large-cate/{code}")
    fun getItemsByLargeCate(@PathVariable code: String): Flow<TestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getItemsByLargeCate(code).collect { emit(it) }
        }
    }

    // --- StandardCharge ---

    @PostMapping("/charge")
    fun registerCharge(
        @RequestBody request: StandardChargeRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<StandardChargeResponse> = mono {
        useCase.registerCharge(request, auth.adminId)
    }

    @GetMapping("/charge/{id}")
    fun getCharge(@PathVariable id: String): Mono<StandardChargeResponse> = mono {
        useCase.getCharge(id)
    }

    @DeleteMapping("/charge/{id}")
    fun deleteCharge(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteCharge(id, auth.adminId)
        null
    }

    @GetMapping("/charge/by-test/{tstCd}")
    fun getChargesByTest(@PathVariable tstCd: String): Flow<StandardChargeResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getChargesByTest(tstCd).collect { emit(it) }
        }
    }

    // --- TestItemSpecimen ---

    @PostMapping("/specimen")
    fun registerSpecimen(
        @RequestBody request: TestItemSpecimenRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemSpecimenResponse> = mono {
        useCase.registerSpecimen(request, auth.adminId)
    }

    @GetMapping("/specimen/{id}")
    fun getSpecimen(@PathVariable id: String): Mono<TestItemSpecimenResponse> = mono {
        useCase.getSpecimen(id)
    }

    @DeleteMapping("/specimen/{id}")
    fun deleteSpecimen(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteSpecimen(id, auth.adminId)
        null
    }

    @GetMapping("/specimen/by-test/{tstCd}")
    fun getSpecimensByTest(@PathVariable tstCd: String): Flow<TestItemSpecimenResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getSpecimensByTest(tstCd).collect { emit(it) }
        }
    }
}
