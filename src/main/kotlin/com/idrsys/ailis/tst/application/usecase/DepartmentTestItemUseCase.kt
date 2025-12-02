package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import kotlinx.coroutines.flow.Flow

interface DepartmentTestItemUseCase {
    // --- DepartmentGroup ---
    suspend fun registerGroup(request: DepartmentGroupRegisterRequest, adminId: String): DepartmentGroupResponse
    suspend fun getGroup(deptGroupId: String): DepartmentGroupResponse
    suspend fun updateGroup(deptGroupId: String, request: DepartmentGroupUpdateRequest, adminId: String): DepartmentGroupResponse
    suspend fun deleteGroup(deptGroupId: String, adminId: String)
    suspend fun getAllGroups(): Flow<DepartmentGroupResponse>

    // --- DepartmentGroupItem ---
    suspend fun registerGroupItem(request: DepartmentGroupItemRegisterRequest, adminId: String): DepartmentGroupItemResponse
    suspend fun getGroupItem(deptGrpItmId: String): DepartmentGroupItemResponse
    suspend fun updateGroupItem(deptGrpItmId: String, request: DepartmentGroupItemUpdateRequest, adminId: String): DepartmentGroupItemResponse
    suspend fun deleteGroupItem(deptGrpItmId: String, adminId: String)
    suspend fun getGroupItemsByDept(deptCd: String): Flow<DepartmentGroupItemResponse>

    // --- DepartmentGroupItemTest ---
    suspend fun registerGroupItemTest(request: DepartmentGroupItemTestRegisterRequest, adminId: String): DepartmentGroupItemTestResponse
    suspend fun deleteGroupItemTest(deptGrpItmTstId: String, adminId: String)
    suspend fun getGroupItemTestsByDept(deptCd: String): Flow<DepartmentGroupItemTestResponse>

    // --- DepartmentTestItem ---
    suspend fun registerTestItem(request: DepartmentTestItemRegisterRequest, adminId: String): DepartmentTestItemResponse
    suspend fun updateTestItem(deptTstItemId: String, request: DepartmentTestItemUpdateRequest, adminId: String): DepartmentTestItemResponse
    suspend fun deleteTestItem(deptTstItemId: String, adminId: String)
    suspend fun getTestItemsByDept(searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse>
}
