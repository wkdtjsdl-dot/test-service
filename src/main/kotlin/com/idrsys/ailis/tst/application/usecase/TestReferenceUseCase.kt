package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestReferenceUseCase {
    // --- TestReference ---
    suspend fun registerReference(request: TestReferenceRegisterRequest, adminId: String): TestReferenceResponse
    suspend fun getReference(refCd: String): TestReferenceResponse
    suspend fun updateReference(refCd: String, request: TestReferenceUpdateRequest, adminId: String): TestReferenceResponse
    suspend fun deleteReference(refCd: String, adminId: String)
    suspend fun getAllReferences(refCateCd: String?): Flow<TestReferenceResponse>
    fun autoCompleteReferences(searchParam: TestReferenceAutoCompleteParam): Flow<TestReferenceAutoCompleteResponse>
    suspend fun findSimpleReferenceByRefCd(refCds: List<String>): Flow<TestReferenceSimpleResponse>

    // --- TestReferenceGroup ---
    suspend fun registerGroup(request: TestReferenceGroupRegisterRequest, adminId: String): TestReferenceGroupResponse
    suspend fun getGroup(refGroupCd: String): TestReferenceGroupResponse
    suspend fun updateGroup(refGroupCd: String, request: TestReferenceGroupUpdateRequest, adminId: String): TestReferenceGroupResponse
    suspend fun deleteGroup(refGroupCd: String, adminId: String)
    suspend fun getAllGroups(): Flow<TestReferenceGroupResponse>

    // --- TestReferenceGroupItem ---
    suspend fun registerGroupItem(request: TestReferenceGroupItemRegisterRequest, adminId: String): TestReferenceGroupItemResponse
    suspend fun getGroupItem(tstRefGroupItemId: String): TestReferenceGroupItemResponse
    suspend fun updateGroupItem(tstRefGroupItemId: String, request: TestReferenceGroupItemUpdateRequest, adminId: String): TestReferenceGroupItemResponse
    suspend fun deleteGroupItem(tstRefGroupItemId: String, adminId: String)
    fun getGroupItemsByGroup(refGroupCd: String): Flow<TestReferenceGroupItemResponse>
}
