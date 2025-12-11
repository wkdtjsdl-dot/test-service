package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.dto.request.UnspecifiedDepartmentTestItemSearchParam
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestGene
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemEssentialDoc
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemGene
import com.idrsys.ailis.tst.domain.model.TestItemHst
import kotlinx.coroutines.flow.Flow

interface TestItemRepository {
    // --- TestItem ---
    suspend fun save(entity: TestItem): TestItem
    suspend fun findById(tstCd: String): TestItem?
    fun getItems(searchParam: TestItemSearchParam): Flow<TestItem>
    fun findUnspecifiedDeptItems(searchParam: UnspecifiedDepartmentTestItemSearchParam): Flow<TestItem>
    fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemAutoCompleteResponse>

    // --- StandardCharge ---
    suspend fun saveCharge(entity: StandardCharge): StandardCharge
    suspend fun findChargeById(id: String): StandardCharge?
    suspend fun deleteChargeById(id: String)
    suspend fun findChargesByTestCd(tstCd: String): Flow<StandardCharge>

    // --- TestItemSpecimen ---
    suspend fun saveSpecimen(entity: TestItemSpecimen): TestItemSpecimen
    suspend fun findSpecimenById(spcmId: String): TestItemSpecimen?
    suspend fun deleteSpecimenById(id: String)
    fun findSpecimensByTestCd(tstCd: String): Flow<TestItemSpecimen>

    // --- TestItemRefItem ---
    suspend fun saveRefItem(entity: TestItemRefItem): TestItemRefItem
    suspend fun findRefItemById(refItemId: String): TestItemRefItem?
    suspend fun deleteRefItemById(refItemId: String)
    fun findRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse>
    suspend fun getDetailRefItemById(refItemId: String): TestItemRefDetailResponse?

    // --- TestGene ---
     fun getGenes(genAlpa: String):Flow<TestGene>
    // --- TestItemGene ---
    suspend fun saveGene(entity: TestItemGene): TestItemGene
    suspend fun deleteGeneById(itemGeneId: String)
    fun findGenesByTestCd(tstCd: String): Flow<TestItemGeneResponse>

    // --- TestItemEssentialDoc ---
    suspend fun saveEssentialDoc(entity: TestItemEssentialDoc): TestItemEssentialDoc
    suspend fun findEssentialDocById(itemEstlDocId: String): TestItemEssentialDoc?
    suspend fun deleteEssentialDocById(itemEstlDocId: String)
    fun findEssentialDocsByTstCd(tstCd: String): Flow<TestItemEssentialDoc>

    // --- TestItemHst ---
    suspend fun saveTestItemHistory(entity: TestItemHst): TestItemHst
}
