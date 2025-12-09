package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.DepartmentGroupItemWithCount
import com.idrsys.ailis.tst.application.dto.DeptTestItemCategoryResponse
import com.idrsys.ailis.tst.application.dto.request.DepartmentGroupItemSearchParam
import com.idrsys.ailis.tst.application.dto.request.DepartmentGroupItemTestSearchParam
import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import kotlinx.coroutines.flow.Flow

interface DepartmentTestItemRepository {
    // --- DepartmentGroup ---
    suspend fun saveGroup(entity: DepartmentGroup): DepartmentGroup
    suspend fun findGroupById(deptGroupId: String): DepartmentGroup?
    suspend fun deleteGroupById(deptGroupId: String)
    suspend fun findAllGroups(): Flow<DepartmentGroup>
    suspend fun findlGroupsByDeptCd(deptCd: String): Flow<DepartmentGroup>

    // --- DepartmentGroupItem ---
    suspend fun saveGroupItem(entity: DepartmentGroupItem): DepartmentGroupItem
    suspend fun findGroupItemById(deptGrpItmId: String): DepartmentGroupItem?
    suspend fun deleteGroupItemById(deptGrpItmId: String)
    suspend fun findGroupItemsByDeptCd(deptCd: String): Flow<DepartmentGroupItem>
    suspend fun getGroupItems(search: DepartmentGroupItemSearchParam): Flow<DepartmentGroupItemWithCount>


    // --- DepartmentGroupItemTest ---
    suspend fun saveGroupItemTest(entity: DepartmentGroupItemTest): DepartmentGroupItemTest
    suspend fun deleteGroupItemTestById(deptGrpItmTstId: String)
    suspend fun findGroupItemTestsByDeptCd(searchParam: DepartmentGroupItemTestSearchParam): Flow<DepartmentGroupItemTest>

    // --- DepartmentTestItem ---
    suspend fun saveTestItem(entity: DepartmentTestItem): DepartmentTestItem
    suspend fun findTestItemById(deptTstItemId: String): DepartmentTestItem?
    suspend fun deleteTestItemById(deptTstItemId: String)
    suspend fun findTestItemsByDeptCd(searchParam: DepartmentTestItemSearchParam): Flow<DeptTestItemCategoryResponse>
}
