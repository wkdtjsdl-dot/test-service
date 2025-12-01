package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import kotlinx.coroutines.flow.Flow

interface DepartmentTestItemUseCase {
    // --- DepartmentGroup ---
    suspend fun registerGroup(request: DepartmentGroupRegisterRequest, adminId: String): DepartmentGroupResponse
    suspend fun getGroup(id: String): DepartmentGroupResponse
    suspend fun updateGroup(id: String, request: DepartmentGroupUpdateRequest, adminId: String): DepartmentGroupResponse
    suspend fun deleteGroup(id: String, adminId: String)
    suspend fun getAllGroups(): Flow<DepartmentGroupResponse>

    // --- DepartmentGroupItem ---
    suspend fun registerGroupItem(request: DepartmentGroupItemRegisterRequest, adminId: String): DepartmentGroupItemResponse
    suspend fun getGroupItem(id: String): DepartmentGroupItemResponse
    suspend fun updateGroupItem(id: String, request: DepartmentGroupItemUpdateRequest, adminId: String): DepartmentGroupItemResponse
    suspend fun deleteGroupItem(id: String, adminId: String)
    suspend fun getGroupItemsByDept(deptCd: String): Flow<DepartmentGroupItemResponse>

    // --- DepartmentGroupItemTest ---
    suspend fun registerGroupItemTest(request: DepartmentGroupItemTestRegisterRequest, adminId: String): DepartmentGroupItemTestResponse
    suspend fun getGroupItemTest(id: String): DepartmentGroupItemTestResponse
    suspend fun deleteGroupItemTest(id: String, adminId: String)
    suspend fun getGroupItemTestsByDept(deptCd: String): Flow<DepartmentGroupItemTestResponse>

    // --- DepartmentTestItem ---
    suspend fun registerTestItem(request: DepartmentTestItemRegisterRequest, adminId: String): DepartmentTestItemResponse
    suspend fun getTestItem(id: String): DepartmentTestItemResponse
    suspend fun updateTestItem(id: String, request: DepartmentTestItemUpdateRequest, adminId: String): DepartmentTestItemResponse
    suspend fun deleteTestItem(id: String, adminId: String)
    suspend fun getTestItemsByDept(searchParam: DepartmentTestItemSearchParam): Flow<DepartmentTestItemResponse>
}
