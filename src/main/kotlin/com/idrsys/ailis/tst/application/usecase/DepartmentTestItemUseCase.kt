package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface DepartmentTestItemUseCase {
    // --- DepartmentGroup ---
    suspend fun registerGroup(request: DepartmentGroupRegisterRequest): DepartmentGroupResponse
    suspend fun getGroup(id: String): DepartmentGroupResponse
    suspend fun updateGroup(id: String, request: DepartmentGroupUpdateRequest): DepartmentGroupResponse
    suspend fun deleteGroup(id: String)
    suspend fun getAllGroups(): Flow<DepartmentGroupResponse>

    // --- DepartmentGroupItem ---
    suspend fun registerGroupItem(request: DepartmentGroupItemRegisterRequest): DepartmentGroupItemResponse
    suspend fun getGroupItem(id: String): DepartmentGroupItemResponse
    suspend fun updateGroupItem(id: String, request: DepartmentGroupItemUpdateRequest): DepartmentGroupItemResponse
    suspend fun deleteGroupItem(id: String)
    suspend fun getGroupItemsByDept(deptCd: String): Flow<DepartmentGroupItemResponse>

    // --- DepartmentGroupItemTest ---
    suspend fun registerGroupItemTest(request: DepartmentGroupItemTestRegisterRequest): DepartmentGroupItemTestResponse
    suspend fun getGroupItemTest(id: String): DepartmentGroupItemTestResponse
    suspend fun deleteGroupItemTest(id: String)
    suspend fun getGroupItemTestsByDept(deptCd: String): Flow<DepartmentGroupItemTestResponse>

    // --- DepartmentTestItem ---
    suspend fun registerTestItem(request: DepartmentTestItemRegisterRequest): DepartmentTestItemResponse
    suspend fun getTestItem(id: String): DepartmentTestItemResponse
    suspend fun updateTestItem(id: String, request: DepartmentTestItemUpdateRequest): DepartmentTestItemResponse
    suspend fun deleteTestItem(id: String)
    suspend fun getTestItemsByDept(deptCd: String): Flow<DepartmentTestItemResponse>
}
