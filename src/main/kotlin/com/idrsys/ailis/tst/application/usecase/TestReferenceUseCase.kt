package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestReferenceUseCase {
    // --- TestReference ---
    suspend fun registerReference(request: TestReferenceRegisterRequest, adminId: String): TestReferenceResponse
    suspend fun getReference(id: String): TestReferenceResponse
    suspend fun updateReference(id: String, request: TestReferenceUpdateRequest, adminId: String): TestReferenceResponse
    suspend fun deleteReference(id: String, adminId: String)
    suspend fun getAllReferences(): Flow<TestReferenceResponse>

    // --- TestReferenceGroup ---
    suspend fun registerGroup(request: TestReferenceGroupRegisterRequest, adminId: String): TestReferenceGroupResponse
    suspend fun getGroup(id: String): TestReferenceGroupResponse
    suspend fun updateGroup(id: String, request: TestReferenceGroupUpdateRequest, adminId: String): TestReferenceGroupResponse
    suspend fun deleteGroup(id: String, adminId: String)
    suspend fun getAllGroups(): Flow<TestReferenceGroupResponse>

    // --- TestReferenceGroupItem ---
    suspend fun registerGroupItem(request: TestReferenceGroupItemRegisterRequest, adminId: String): TestReferenceGroupItemResponse
    suspend fun getGroupItem(id: String): TestReferenceGroupItemResponse
    suspend fun updateGroupItem(id: String, request: TestReferenceGroupItemUpdateRequest, adminId: String): TestReferenceGroupItemResponse
    suspend fun deleteGroupItem(id: String, adminId: String)
    fun getGroupItemsByGroup(groupCd: String): Flow<TestReferenceGroupItemResponse>
}
