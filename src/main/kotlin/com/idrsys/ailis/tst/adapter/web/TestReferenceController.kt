package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "Test Reference", description = "검사 항목 관리 API")
@RestController
@RequestMapping("/tst/ref")
class TestReferenceController(
    private val useCase: TestReferenceUseCase
) {

    // --- TestReference ---

    @PostMapping
    fun registerReference(@RequestBody request: TestReferenceRegisterRequest): Mono<TestReferenceResponse> = mono {
        useCase.registerReference(request)
    }

    @GetMapping("/{id}")
    fun getReference(@PathVariable id: String): Mono<TestReferenceResponse> = mono {
        useCase.getReference(id)
    }

    @PutMapping("/{id}")
    fun updateReference(@PathVariable id: String, @RequestBody request: TestReferenceUpdateRequest): Mono<TestReferenceResponse> = mono {
        useCase.updateReference(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteReference(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteReference(id)
        null
    }

    @GetMapping
    fun getAllReferences(): Flow<TestReferenceResponse> {
        // Flow is natively supported by WebFlux, no need to wrap in Mono
        return kotlinx.coroutines.flow.flow {
            useCase.getAllReferences().collect { emit(it) }
        }
    }

    // --- TestReferenceGroup ---

    @PostMapping("/group")
    fun registerGroup(@RequestBody request: TestReferenceGroupRegisterRequest): Mono<TestReferenceGroupResponse> = mono {
        useCase.registerGroup(request)
    }

    @GetMapping("/group/{id}")
    fun getGroup(@PathVariable id: String): Mono<TestReferenceGroupResponse> = mono {
        useCase.getGroup(id)
    }

    @PutMapping("/group/{id}")
    fun updateGroup(@PathVariable id: String, @RequestBody request: TestReferenceGroupUpdateRequest): Mono<TestReferenceGroupResponse> = mono {
        useCase.updateGroup(id, request)
    }

    @DeleteMapping("/group/{id}")
    fun deleteGroup(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteGroup(id)
        null
    }

    @GetMapping("/group")
    fun getAllGroups(): Flow<TestReferenceGroupResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllGroups().collect { emit(it) }
        }
    }

    // --- TestReferenceGroupItem ---

    @PostMapping("/group-item")
    fun registerGroupItem(@RequestBody request: TestReferenceGroupItemRegisterRequest): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.registerGroupItem(request)
    }

    @GetMapping("/group-item/{id}")
    fun getGroupItem(@PathVariable id: String): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.getGroupItem(id)
    }

    @PutMapping("/group-item/{id}")
    fun updateGroupItem(@PathVariable id: String, @RequestBody request: TestReferenceGroupItemUpdateRequest): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.updateGroupItem(id, request)
    }

    @DeleteMapping("/group-item/{id}")
    fun deleteGroupItem(@PathVariable id: String): Mono<Void> = mono {
        useCase.deleteGroupItem(id)
        null
    }

    @GetMapping("/group-item/by-group/{groupCd}")
    fun getGroupItemsByGroup(@PathVariable groupCd: String): Flow<TestReferenceGroupItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemsByGroup(groupCd).collect { emit(it) }
        }
    }
}
