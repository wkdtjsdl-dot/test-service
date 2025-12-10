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
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "Test Reference", description = "검사 항목 관리 API")
@RestController
class TestReferenceController(
    private val useCase: TestReferenceUseCase
) {

    // --- TestReference ---

    @Operation(summary = "검사 기준정보 검사참조항목 등록", description = "새로운 검사 항목을 등록합니다")
    @PostMapping("/api/bbs/tst-ref")
    fun registerReference(
        @RequestBody request: TestReferenceRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceResponse> = mono {
        useCase.registerReference(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 검사참조항목 조회", description = "ID로 검사 항목을 조회합니다")
    @GetMapping("/api/bbs/tst-ref/{refCd}")
    fun getReference(@PathVariable refCd: String): Mono<TestReferenceResponse> = mono {
        useCase.getReference(refCd)
    }

    @Operation(summary = "검사 기준정보 검사참조항목 수정", description = "검사 항목 정보를 수정합니다")
    @PutMapping("/api/bbs/tst-ref/{refCd}")
    fun updateReference(
        @PathVariable refCd: String,
        @RequestBody request: TestReferenceUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceResponse> = mono {
        useCase.updateReference(refCd, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 검사참조항목 삭제", description = "검사 항목을 삭제합니다")
    @DeleteMapping("/api/bbs/tst-ref/{refCd}")
    fun deleteReference(
        @PathVariable refCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteReference(refCd, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 검사참조항목 목록", description = "모든 검사 항목을 조회합니다")
    @GetMapping("/api/bbs/tst-ref")
    fun getAllReferences(
        @RequestParam(required = false) refCateCd: String?,
    ): Flow<TestReferenceResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllReferences(refCateCd).collect { emit(it) }
        }
    }

    @Operation(summary = "참조항목 코드 자동완성")
    @GetMapping("/api/bbs/tst-ref/auto-complete")
    fun autoCompleteReference(@ParameterObject searchParam: TestReferenceAutoCompleteParam): Flow<TestReferenceAutoCompleteResponse> {
        return useCase.autoCompleteReferences(searchParam)
    }

    // --- TestReferenceGroup ---

    @Operation(summary = "검사 기준정보 검사 참조그룹 등록", description = "새로운 검사 항목 그룹을 등록합니다")
    @PostMapping("/api/bbs/tst-ref-group")
    fun registerGroup(
        @RequestBody request: TestReferenceGroupRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupResponse> = mono {
        useCase.registerGroup(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 검사 참조그룹 조회")
    @GetMapping("/api/bbs/tst-ref-group/{refGroupCd}")
    fun getGroup(@PathVariable refGroupCd: String): Mono<TestReferenceGroupResponse> = mono {
        useCase.getGroup(refGroupCd)
    }

    @Operation(summary = "검사 기준정보 검사 참조그룹 수정")
    @PutMapping("/api/bbs/tst-ref-group/{refGroupCd}")
    fun updateGroup(
        @PathVariable refGroupCd: String,
        @RequestBody request: TestReferenceGroupUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupResponse> = mono {
        useCase.updateGroup(refGroupCd, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 검사 참조그룹 삭제")
    @DeleteMapping("/api/bbs/tst-ref-group/{refGroupCd}")
    fun deleteGroup(
        @PathVariable refGroupCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroup(refGroupCd, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 검사 참조그룹 목록")
    @GetMapping("/api/bbs/tst-ref-group")
    fun getAllGroups(): Flow<TestReferenceGroupResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllGroups().collect { emit(it) }
        }
    }

    // --- TestReferenceGroupItem ---

    @Operation(summary = "검사 기준정보 검사 참조그룹 참조항목 추가")
    @PostMapping("/api/bbs/tst-ref-group-item")
    fun registerGroupItem(
        @RequestBody request: TestReferenceGroupItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.registerGroupItem(request, auth.adminId)
    }

    @Operation(summary = "검사 항목 그룹 항목 조회")
    @GetMapping("/api/bbs/tst-ref-group-item/{tstRefGroupItemId}")
    fun getGroupItem(@PathVariable tstRefGroupItemId: String): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.getGroupItem(tstRefGroupItemId)
    }

    @Operation(summary = "검사 항목 그룹 항목 수정")
    @PutMapping("/api/bbs/tst-ref-group-item/{tstRefGroupItemId}")
    fun updateGroupItem(
        @PathVariable tstRefGroupItemId: String,
        @RequestBody request: TestReferenceGroupItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestReferenceGroupItemResponse> = mono {
        useCase.updateGroupItem(tstRefGroupItemId, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 검사 참조그룹 참조항목 삭제")
    @DeleteMapping("/api/bbs/tst-ref-group-item/{tstRefGroupItemId}")
    fun deleteGroupItem(
        @PathVariable tstRefGroupItemId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItem(tstRefGroupItemId, auth.adminId)
        null
    }

    @Operation(summary = "검사 항목 그룹 항목 목록 조회 (그룹코드별)")
    @GetMapping("/api/bbs/tst-ref-group-item")
    fun getGroupItemsByGroup(@RequestParam refGroupCd: String): Flow<TestReferenceGroupItemResponse> =
        useCase.getGroupItemsByGroup(refGroupCd)
}
