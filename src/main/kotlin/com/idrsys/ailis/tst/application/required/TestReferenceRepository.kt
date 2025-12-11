package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.TestReferenceAutoCompleteParam
import com.idrsys.ailis.tst.application.dto.TestReferenceAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.TestReferenceGroupItemResponse
import com.idrsys.ailis.tst.application.dto.TestReferenceSimpleResponse
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import kotlinx.coroutines.flow.Flow

interface TestReferenceRepository {
    // --- TestReference ---
    suspend fun save(entity: TestReference): TestReference
    suspend fun findById(refCd: String): TestReference?
    suspend fun findAllByRefCateCd(refCateCd: String?): Flow<TestReference>
    fun autoCompleteReferences(searchParam: TestReferenceAutoCompleteParam): Flow<TestReferenceAutoCompleteResponse>
    suspend fun findSimpleReferenceByRefCd(refCds: List<String>): Flow<TestReferenceSimpleResponse>

    // --- TestReferenceGroup ---
    suspend fun saveGroup(entity: TestReferenceGroup): TestReferenceGroup
    suspend fun findGroupById(refGroupCd: String): TestReferenceGroup?
    suspend fun deleteGroupById(refGroupCd: String)
    suspend fun findAllGroups(): Flow<TestReferenceGroup>

    // --- TestReferenceGroupItem ---
    suspend fun saveGroupItem(entity: TestReferenceGroupItem): TestReferenceGroupItem
    suspend fun findGroupItemById(tstRefGroupItemId: String): TestReferenceGroupItem?
    suspend fun deleteGroupItemById(tstRefGroupItemId: String)
    fun findGroupItemsByGroupCd(refGroupCd: String): Flow<TestReferenceGroupItemResponse>
}
