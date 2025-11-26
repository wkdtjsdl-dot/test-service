package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestReferenceUseCase {
    // --- TestReference ---
    suspend fun registerReference(request: TestReferenceRegisterRequest): TestReferenceResponse
    suspend fun getReference(id: String): TestReferenceResponse
    suspend fun updateReference(id: String, request: TestReferenceUpdateRequest): TestReferenceResponse
    suspend fun deleteReference(id: String)
    suspend fun getAllReferences(): Flow<TestReferenceResponse>

    // --- TestReferenceGroup ---
    suspend fun registerGroup(request: TestReferenceGroupRegisterRequest): TestReferenceGroupResponse
    suspend fun getGroup(id: String): TestReferenceGroupResponse
    suspend fun updateGroup(id: String, request: TestReferenceGroupUpdateRequest): TestReferenceGroupResponse
    suspend fun deleteGroup(id: String)
    suspend fun getAllGroups(): Flow<TestReferenceGroupResponse>

    // --- TestReferenceGroupItem ---
    suspend fun registerGroupItem(request: TestReferenceGroupItemRegisterRequest): TestReferenceGroupItemResponse
    suspend fun getGroupItem(id: String): TestReferenceGroupItemResponse
    suspend fun updateGroupItem(id: String, request: TestReferenceGroupItemUpdateRequest): TestReferenceGroupItemResponse
    suspend fun deleteGroupItem(id: String)
    suspend fun getGroupItemsByGroup(groupCd: String): Flow<TestReferenceGroupItemResponse>
}
