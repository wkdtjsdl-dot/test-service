package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.DepartmentGroupItemSearchParam
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

    @Operation(summary = "검사 기준정보 부서분류그룹 등록")
    @PostMapping("/api/bbs/dept-group")
    fun registerGroup(
        @RequestBody request: DepartmentGroupRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
        useCase.registerGroup(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹 조회")
    @GetMapping("/api/bbs/dept-group/{deptGroupId}")
    fun getGroup(@PathVariable deptGroupId: String): Mono<DepartmentGroupResponse> = mono {
        useCase.getGroup(deptGroupId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹 수정")
    @PutMapping("/api/bbs/dept-group/{deptGroupId}")
    fun updateGroup(
        @PathVariable deptGroupId: String,
        @RequestBody request: DepartmentGroupUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
        useCase.updateGroup(deptGroupId, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹 삭제")
    @DeleteMapping("/api/bbs/dept-group/{deptGroupId}")
    fun deleteGroup(
        @PathVariable deptGroupId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroup(deptGroupId, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 부서분류그룹 목록")
    @GetMapping("/api/bbs/dept-group")
    fun getAllGroups(@RequestParam(required = false) deptCd: String): Flow<DepartmentGroupResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroups(deptCd).collect { emit(it) }
        }
    }



    // --- DepartmentGroupItem ---

    @Operation(summary = "검사 기준정보 부서분류그룹항목 등록")
    @PostMapping("/api/bbs/dept-group-item")
    fun registerGroupItem(
        @RequestBody request: DepartmentGroupItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
        useCase.registerGroupItem(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹항목 조회")
    @GetMapping("/api/bbs/dept-group-item/{deptGrpItmId}")
    fun getGroupItem(@PathVariable deptGrpItmId: String): Mono<DepartmentGroupItemResponse> = mono {
        useCase.getGroupItem(deptGrpItmId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹항목 수정")
    @PutMapping("/api/bbs/dept-group-item/{deptGrpItmId}")
    fun updateGroupItem(
        @PathVariable deptGrpItmId: String,
        @RequestBody request: DepartmentGroupItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
        useCase.updateGroupItem(deptGrpItmId, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서분류그룹항목 삭제")
    @DeleteMapping("/api/bbs/dept-group-item/{deptGrpItmId}")
    fun deleteGroupItem(
        @PathVariable deptGrpItmId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItem(deptGrpItmId, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 부서분류그룹항목 목록")
    @GetMapping("/api/bbs/dept-group-item")
    fun getGroupItems(
        @ParameterObject search: DepartmentGroupItemSearchParam
    ): Flow<DepartmentGroupItemWithCount> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItems(search).collect { emit(it) }
        }
    }


    // --- DepartmentGroupItemTest ---

    @Operation(summary = "검사 기준정보 부서검사종목 등록")
    @PostMapping("/api/bts/dept-group-item-tst")
    fun registerGroupItemTest(
        @RequestBody request: DepartmentGroupItemTestRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemTestResponse> = mono {
        useCase.registerGroupItemTest(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서검사종목 삭제")
    @DeleteMapping("/api/bts/dept-group-item-tst/{deptGrpItmTstId}")
    fun deleteGroupItemTest(
        @PathVariable deptGrpItmTstId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItemTest(deptGrpItmTstId, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 부서검사종목 목록")
    @GetMapping("/api/bts/dept-group-item-tst/by-dept/{deptCd}")
    fun getGroupItemTestsByDept(@PathVariable deptCd: String): Flow<DepartmentGroupItemTestResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemTestsByDept(deptCd).collect { emit(it) }
        }
    }

    // --- DepartmentTestItem ---

    @Operation(summary = "검사 기준정보 부서검사종목 등록")
    @PostMapping("/api/bbs/dept-tst-item")
    fun registerTestItem(
        @RequestBody request: DepartmentTestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.registerTestItem(request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서검사종목 수정")
    @PutMapping("/api/bbs/dept-tst-item/{deptTstItemId}")
    fun updateTestItem(
        @PathVariable deptTstItemId: String,
        @RequestBody request: DepartmentTestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.updateTestItem(deptTstItemId, request, auth.adminId)
    }

    @Operation(summary = "검사 기준정보 부서검사종목 삭제")
    @DeleteMapping("/api/bbs/dept-tst-item/{deptTstItemId}")
    fun deleteTestItem(
        @PathVariable deptTstItemId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteTestItem(deptTstItemId, auth.adminId)
        null
    }

    @Operation(summary = "검사 기준정보 부서검사종목 목록")
    @GetMapping("/api/bbs/dept-tst-item")
    fun getTestItemsByDept(@ParameterObject searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getTestItemsByDept(searchParam).collect { emit(it) }
        }
    }
}
