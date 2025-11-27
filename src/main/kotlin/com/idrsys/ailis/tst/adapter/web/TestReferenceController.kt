package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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

    @Operation(summary = "검사 항목 등록", description = "새로운 검사 항목을 등록합니다")
    @PostMapping
    fun registerReference(
        @RequestBody request: TestReferenceRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceResponse> = mono {
        useCase.registerReference(request, auth.adminId)
    }

    @Operation(summary = "검사 항목 조회", description = "ID로 검사 항목을 조회합니다")
    @GetMapping("/{id}")
    fun getReference(@PathVariable id: String): Mono<TestReferenceResponse> = mono {
        useCase.getReference(id)
    }

    @Operation(summary = "검사 항목 수정", description = "검사 항목 정보를 수정합니다")
    @PutMapping("/{id}")
    fun updateReference(
        @PathVariable id: String,
        @RequestBody request: TestReferenceUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceResponse> = mono {
        useCase.updateReference(id, request, auth.adminId)
    }

    @Operation(summary = "검사 항목 삭제", description = "검사 항목을 삭제합니다")
    @DeleteMapping("/{id}")
    fun deleteReference(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteReference(id, auth.adminId)
        null
    }

    @Operation(summary = "검사 항목 전체 조회", description = "모든 검사 항목을 조회합니다")
    @GetMapping
    fun getAllReferences(): Flow<TestReferenceResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllReferences().collect { emit(it) }
        }
    }

    // --- TestReferenceGroup ---

    @Operation(summary = "검사 항목 그룹 등록", description = "새로운 검사 항목 그룹을 등록합니다")
    @PostMapping("/group")
    fun registerGroup(
        @RequestBody request: TestReferenceGroupRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupResponse> = mono {
        useCase.registerGroup(request, auth.adminId)
    }

    @GetMapping("/group/{id}")
    fun getGroup(@PathVariable id: String): Mono<TestReferenceGroupResponse> = mono {
        useCase.getGroup(id)
    }

    @PutMapping("/group/{id}")
    fun updateGroup(
        @PathVariable id: String,
        @RequestBody request: TestReferenceGroupUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupResponse> = mono {
        useCase.updateGroup(id, request, auth.adminId)
    }

    @DeleteMapping("/group/{id}")
    fun deleteGroup(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroup(id, auth.adminId)
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
    fun registerGroupItem(
        @RequestBody request: TestReferenceGroupItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.registerGroupItem(request, auth.adminId)
    }

    @GetMapping("/group-item/{id}")
    fun getGroupItem(@PathVariable id: String): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.getGroupItem(id)
    }

    @PutMapping("/group-item/{id}")
    fun updateGroupItem(
        @PathVariable id: String,
        @RequestBody request: TestReferenceGroupItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.updateGroupItem(id, request, auth.adminId)
    }

    @DeleteMapping("/group-item/{id}")
    fun deleteGroupItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItem(id, auth.adminId)
        null
    }

    @GetMapping("/group-item/by-group/{groupCd}")
    fun getGroupItemsByGroup(@PathVariable groupCd: String): Flow<TestReferenceGroupItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemsByGroup(groupCd).collect { emit(it) }
        }
    }
}
