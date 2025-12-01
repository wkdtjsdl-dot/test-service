package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import kotlinx.coroutines.flow.Flow

interface TestReferenceRepository {
    // --- TestReference ---
    suspend fun save(entity: TestReference): TestReference
    suspend fun findById(id: String): TestReference?
    suspend fun deleteById(id: String)
    suspend fun findAll(): Flow<TestReference>

    // --- TestReferenceGroup ---
    suspend fun saveGroup(entity: TestReferenceGroup): TestReferenceGroup
    suspend fun findGroupById(id: String): TestReferenceGroup?
    suspend fun deleteGroupById(id: String)
    suspend fun findAllGroups(): Flow<TestReferenceGroup>

    // --- TestReferenceGroupItem ---
    suspend fun saveGroupItem(entity: TestReferenceGroupItem): TestReferenceGroupItem
    suspend fun findGroupItemById(id: String): TestReferenceGroupItem?
    suspend fun deleteGroupItemById(id: String)
    fun findGroupItemsByGroupCd(groupCd: String): Flow<TestReferenceGroupItem>
}
