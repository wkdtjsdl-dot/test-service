package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import io.swagger.v3.oas.annotations.Operation
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
    fun registerItem(@RequestBody request: TestItemRegisterRequest): Mono<TestItemResponse> = mono {
        useCase.registerItem(request)
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: String): Mono<TestItemResponse> = mono {
        useCase.getItem(id)
    }

    @PutMapping("/{id}")
    fun updateItem(@PathVariable id: String, @RequestBody request: TestItemUpdateRequest): Mono<TestItemResponse> = mono {
        useCase.updateItem(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteItem(id)
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
    fun registerCharge(@RequestBody request: StandardChargeRegisterRequest): Mono<StandardChargeResponse> = mono {
        useCase.registerCharge(request)
    }

    @GetMapping("/charge/{id}")
    fun getCharge(@PathVariable id: String): Mono<StandardChargeResponse> = mono {
        useCase.getCharge(id)
    }

    @DeleteMapping("/charge/{id}")
    fun deleteCharge(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteCharge(id)
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
    fun registerSpecimen(@RequestBody request: TestItemSpecimenRegisterRequest): Mono<TestItemSpecimenResponse> = mono {
        useCase.registerSpecimen(request)
    }

    @GetMapping("/specimen/{id}")
    fun getSpecimen(@PathVariable id: String): Mono<TestItemSpecimenResponse> = mono {
        useCase.getSpecimen(id)
    }

    @DeleteMapping("/specimen/{id}")
    fun deleteSpecimen(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteSpecimen(id)
        null
    }

    @GetMapping("/specimen/by-test/{tstCd}")
    fun getSpecimensByTest(@PathVariable tstCd: String): Flow<TestItemSpecimenResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getSpecimensByTest(tstCd).collect { emit(it) }
        }
    }
}
