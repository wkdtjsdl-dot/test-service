package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.DepartmentTestItemUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tst/dept")
class DepartmentTestItemController(
    private val useCase: DepartmentTestItemUseCase
) {

    // --- DepartmentGroup ---

    @PostMapping("/group")
    fun registerGroup(
        @RequestBody request: DepartmentGroupRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
        useCase.registerGroup(request, auth.adminId)
    }

    @GetMapping("/group/{id}")
    fun getGroup(@PathVariable id: String): Mono<DepartmentGroupResponse> = mono {
        useCase.getGroup(id)
    }

    @PutMapping("/group/{id}")
    fun updateGroup(
        @PathVariable id: String,
        @RequestBody request: DepartmentGroupUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupResponse> = mono {
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
    fun getAllGroups(): Flow<DepartmentGroupResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getAllGroups().collect { emit(it) }
        }
    }

    // --- DepartmentGroupItem ---

    @PostMapping("/group-item")
    fun registerGroupItem(
        @RequestBody request: DepartmentGroupItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
        useCase.registerGroupItem(request, auth.adminId)
    }

    @GetMapping("/group-item/{id}")
    fun getGroupItem(@PathVariable id: String): Mono<DepartmentGroupItemResponse> = mono {
        useCase.getGroupItem(id)
    }

    @PutMapping("/group-item/{id}")
    fun updateGroupItem(
        @PathVariable id: String,
        @RequestBody request: DepartmentGroupItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemResponse> = mono {
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

    @GetMapping("/group-item/by-dept/{deptCd}")
    fun getGroupItemsByDept(@PathVariable deptCd: String): Flow<DepartmentGroupItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemsByDept(deptCd).collect { emit(it) }
        }
    }

    // --- DepartmentGroupItemTest ---

    @PostMapping("/group-item-test")
    fun registerGroupItemTest(
        @RequestBody request: DepartmentGroupItemTestRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentGroupItemTestResponse> = mono {
        useCase.registerGroupItemTest(request, auth.adminId)
    }

    @GetMapping("/group-item-test/{id}")
    fun getGroupItemTest(@PathVariable id: String): Mono<DepartmentGroupItemTestResponse> = mono {
        useCase.getGroupItemTest(id)
    }

    @DeleteMapping("/group-item-test/{id}")
    fun deleteGroupItemTest(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGroupItemTest(id, auth.adminId)
        null
    }

    @GetMapping("/group-item-test/by-dept/{deptCd}")
    fun getGroupItemTestsByDept(@PathVariable deptCd: String): Flow<DepartmentGroupItemTestResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getGroupItemTestsByDept(deptCd).collect { emit(it) }
        }
    }

    // --- DepartmentTestItem ---

    @PostMapping("/test-item")
    fun registerTestItem(
        @RequestBody request: DepartmentTestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.registerTestItem(request, auth.adminId)
    }

    @GetMapping("/test-item/{id}")
    fun getTestItem(@PathVariable id: String): Mono<DepartmentTestItemResponse> = mono {
        useCase.getTestItem(id)
    }

    @PutMapping("/test-item/{id}")
    fun updateTestItem(
        @PathVariable id: String,
        @RequestBody request: DepartmentTestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<DepartmentTestItemResponse> = mono {
        useCase.updateTestItem(id, request, auth.adminId)
    }

    @DeleteMapping("/test-item/{id}")
    fun deleteTestItem(
        @PathVariable id: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteTestItem(id, auth.adminId)
        null
    }

    @GetMapping("/test-item/by-dept/{deptCd}")
    fun getTestItemsByDept(@PathVariable deptCd: String): Flow<DepartmentTestItemResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getTestItemsByDept(deptCd).collect { emit(it) }
        }
    }
}
