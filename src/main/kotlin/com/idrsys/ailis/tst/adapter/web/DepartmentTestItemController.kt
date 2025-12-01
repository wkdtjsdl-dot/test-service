package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.application.usecase.DepartmentTestItemUseCase
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

@Tag(name = "Department Test Item", description = "부서별 검사 항목 관리 API")
@RestController
class DepartmentTestItemController(
    private val useCase: DepartmentTestItemUseCase
) {

    // --- DepartmentGroup ---

    @Operation(summary = "부서별 그룹 등록")
    @PostMapping("/api/bbs/dept-group")
    fun registerGroup(
        @RequestBody request: DepartmentGroupRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
        useCase.registerGroup(request, auth.adminId)
    }

    @Operation(summary = "부서별 그룹 조회")
    @GetMapping("/api/bbs/dept-group/{id}")
    fun getGroup(@PathVariable id: String): Mono<DepartmentGroupResponse> = mono {
        useCase.getGroup(id)
    }

    @Operation(summary = "부서별 그룹 수정")
    @PutMapping("/api/bbs/dept-group/{id}")
    fun updateGroup(
        @PathVariable id: String,
        @RequestBody request: DepartmentGroupUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
        useCase.updateGroup(id, request, auth.adminId)
    }

    @Operation(summary = "부서별 그룹 삭제")
    @DeleteMapping("/api/bbs/dept-group/{id}")
    fun deleteGroup(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroup(id, auth.adminId)
        null
    }

    @Operation(summary = "부서별 그룹 전체 조회")
    @GetMapping("/api/bbs/dept-group")
    fun getAllGroups(): Flow<DepartmentGroupResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllGroups().collect { emit(it) }
        }
    }

    // --- DepartmentGroupItem ---

    @Operation(summary = "부서별 그룹 항목 등록")
    @PostMapping("/api/bbs/dept-group-item")
    fun registerGroupItem(
        @RequestBody request: DepartmentGroupItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
        useCase.registerGroupItem(request, auth.adminId)
    }

    @Operation(summary = "부서별 그룹 항목 조회")
    @GetMapping("/api/bbs/dept-group-item/{id}")
    fun getGroupItem(@PathVariable id: String): Mono<DepartmentGroupItemResponse> = mono {
        useCase.getGroupItem(id)
    }

    @Operation(summary = "부서별 그룹 항목 수정")
    @PutMapping("/api/bbs/dept-group-item/{id}")
    fun updateGroupItem(
        @PathVariable id: String,
        @RequestBody request: DepartmentGroupItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
        useCase.updateGroupItem(id, request, auth.adminId)
    }

    @Operation(summary = "부서별 그룹 항목 삭제")
    @DeleteMapping("/api/bbs/dept-group-item/{id}")
    fun deleteGroupItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItem(id, auth.adminId)
        null
    }

    @Operation(summary = "부서별 그룹 항목 목록 조회 (부서코드별)")
    @GetMapping("/api/bbs/dept-group-item/by-dept/{deptCd}")
    fun getGroupItemsByDept(@PathVariable deptCd: String): Flow<DepartmentGroupItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemsByDept(deptCd).collect { emit(it) }
        }
    }

    // --- DepartmentGroupItemTest ---

    @Operation(summary = "부서별 그룹 항목 검사 등록")
    @PostMapping("/api/bts/dept-group-item-tst")
    fun registerGroupItemTest(
        @RequestBody request: DepartmentGroupItemTestRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemTestResponse> = mono {
        useCase.registerGroupItemTest(request, auth.adminId)
    }

    @Operation(summary = "부서별 그룹 항목 검사 조회")
    @GetMapping("/api/bts/dept-group-item-tst/{id}")
    fun getGroupItemTest(@PathVariable id: String): Mono<DepartmentGroupItemTestResponse> = mono {
        useCase.getGroupItemTest(id)
    }

    @Operation(summary = "부서별 그룹 항목 검사 삭제")
    @DeleteMapping("/api/bts/dept-group-item-tst/{id}")
    fun deleteGroupItemTest(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItemTest(id, auth.adminId)
        null
    }

    @Operation(summary = "부서별 그룹 항목 검사 목록 조회 (부서코드별)")
    @GetMapping("/api/bts/dept-group-item-tst/by-dept/{deptCd}")
    fun getGroupItemTestsByDept(@PathVariable deptCd: String): Flow<DepartmentGroupItemTestResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemTestsByDept(deptCd).collect { emit(it) }
        }
    }

    // --- DepartmentTestItem ---

    @Operation(summary = "부서별 검사 항목 등록")
    @PostMapping("/api/bbs/dept-tst-item")
    fun registerTestItem(
        @RequestBody request: DepartmentTestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.registerTestItem(request, auth.adminId)
    }

    @Operation(summary = "부서별 검사 항목 조회")
    @GetMapping("/api/bbs/dept-tst-item/{id}")
    fun getTestItem(@PathVariable id: String): Mono<DepartmentTestItemResponse> = mono {
        useCase.getTestItem(id)
    }

    @Operation(summary = "부서별 검사 항목 수정")
    @PutMapping("/api/bbs/dept-tst-item/{id}")
    fun updateTestItem(
        @PathVariable id: String,
        @RequestBody request: DepartmentTestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.updateTestItem(id, request, auth.adminId)
    }

    @Operation(summary = "부서별 검사 항목 삭제")
    @DeleteMapping("/api/bbs/dept-tst-item/{id}")
    fun deleteTestItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteTestItem(id, auth.adminId)
        null
    }

    @Operation(summary = "부서별 검사 항목 목록 조회 (부서코드별)")
    @GetMapping("/api/bbs/dept-tst-item")
    fun getTestItemsByDept(@ParameterObject searchParam: DepartmentTestItemSearchParam): Flow<DepartmentTestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getTestItemsByDept(searchParam).collect { emit(it) }
        }
    }
}
