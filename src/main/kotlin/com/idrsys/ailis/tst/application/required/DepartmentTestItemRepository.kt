package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.request.DepartmentTestItemSearchParam
import com.idrsys.ailis.tst.domain.model.DepartmentGroup
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItem
import com.idrsys.ailis.tst.domain.model.DepartmentGroupItemTest
import com.idrsys.ailis.tst.domain.model.DepartmentTestItem
import kotlinx.coroutines.flow.Flow

interface DepartmentTestItemRepository {
    // --- DepartmentGroup ---
    suspend fun saveGroup(entity: DepartmentGroup): DepartmentGroup
    suspend fun findGroupById(id: String): DepartmentGroup?
    suspend fun deleteGroupById(id: String)
    suspend fun findAllGroups(): Flow<DepartmentGroup>

    // --- DepartmentGroupItem ---
    suspend fun saveGroupItem(entity: DepartmentGroupItem): DepartmentGroupItem
    suspend fun findGroupItemById(id: String): DepartmentGroupItem?
    suspend fun deleteGroupItemById(id: String)
    suspend fun findGroupItemsByDeptCd(deptCd: String): Flow<DepartmentGroupItem>

    // --- DepartmentGroupItemTest ---
    suspend fun saveGroupItemTest(entity: DepartmentGroupItemTest): DepartmentGroupItemTest
    suspend fun findGroupItemTestById(id: String): DepartmentGroupItemTest?
    suspend fun deleteGroupItemTestById(id: String)
    suspend fun findGroupItemTestsByDeptCd(deptCd: String): Flow<DepartmentGroupItemTest>

    // --- DepartmentTestItem ---
    suspend fun saveTestItem(entity: DepartmentTestItem): DepartmentTestItem
    suspend fun findTestItemById(id: String): DepartmentTestItem?
    suspend fun deleteTestItemById(id: String)
    suspend fun findTestItemsByDeptCd(searchParam: DepartmentTestItemSearchParam): Flow<DepartmentTestItem>
}
