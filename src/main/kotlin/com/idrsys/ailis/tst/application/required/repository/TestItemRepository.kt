package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.TestGeneRequest
import com.idrsys.ailis.tst.application.dto.TestItemAutoCompleteParam
import com.idrsys.ailis.tst.application.dto.TestItemEssentialDocDetailResponse
import com.idrsys.ailis.tst.application.dto.TestItemEssentialDocListResponse
import com.idrsys.ailis.tst.application.dto.TestItemGeneResponse
import com.idrsys.ailis.tst.application.dto.TestItemRefDetailResponse
import com.idrsys.ailis.tst.application.dto.TestItemRefItemsResponse
import com.idrsys.ailis.tst.application.dto.TestItemRefRequest
import com.idrsys.ailis.tst.application.dto.TestItemRefResponse
import com.idrsys.ailis.tst.application.dto.TestItemSearchParam
import com.idrsys.ailis.tst.application.dto.TestItemSimpleResponse
import com.idrsys.ailis.tst.application.dto.TestItemSpecimenDetailResponse
import com.idrsys.ailis.tst.application.dto.TestItemSpecimenListResponse
import com.idrsys.ailis.tst.application.dto.TestItemSpecimensResponse
import com.idrsys.ailis.tst.application.dto.request.UnspecifiedDepartmentTestItemSearchParam
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestGene
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemEssentialDoc
import com.idrsys.ailis.tst.domain.model.TestItemGene
import com.idrsys.ailis.tst.domain.model.TestItemHst
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.domain.model.TestItemSpecimenHst
import kotlinx.coroutines.flow.Flow

interface TestItemRepository {
    // --- TestItem ---
    suspend fun save(entity: TestItem): TestItem
    suspend fun findById(tstCd: String): TestItem?
    suspend fun findAll(): Flow<TestItem>
    fun getItems(searchParam: TestItemSearchParam): Flow<TestItem>
    fun findUnspecifiedDeptItems(searchParam: UnspecifiedDepartmentTestItemSearchParam): Flow<TestItem>
    fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemSimpleResponse>
    fun findSimpleItemByTstCd(tstCds: List<String>): Flow<TestItemSimpleResponse>

    // --- StandardCharge ---
    suspend fun saveCharge(entity: StandardCharge): StandardCharge
    suspend fun findChargeById(id: String): StandardCharge?
    suspend fun deleteChargeById(id: String)
    suspend fun findChargesByTestCd(tstCd: String): Flow<StandardCharge>
    suspend fun getEqualDate(entity: StandardCharge): Flow<StandardCharge>

    // --- TestItemSpecimen ---
    suspend fun saveSpecimen(entity: TestItemSpecimen): TestItemSpecimen
    suspend fun findSpecimenById(spcmId: String): TestItemSpecimen?
    suspend fun deleteSpecimenById(id: String)
    fun findSpecimensByTestCd(tstCd: String): Flow<TestItemSpecimen>
    suspend fun getSpecimenDetailById(spcmId: String): TestItemSpecimenDetailResponse?
    fun getSpecimenDetailsByTestCd(tstCd: String): Flow<TestItemSpecimenListResponse>
    suspend fun findSpecimensByTstCds(tstCds: List<String>): Flow<TestItemSpecimensResponse>

    // --- TestItemRefItem ---
    suspend fun saveRefItem(entity: TestItemRefItem): TestItemRefItem
    suspend fun findRefItemById(refItemId: String): TestItemRefItem?
    suspend fun deleteRefItemById(refItemId: String)
    fun findRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse>
    suspend fun getDetailRefItemById(refItemId: String): TestItemRefDetailResponse?
    suspend fun findRefItemsByTstCds(tstCds: List<String>): Flow<TestItemRefItemsResponse>

    // --- TestGene ---
     fun getGenes(request: TestGeneRequest): Flow<TestGene>
    // --- TestItemGene ---
    suspend fun saveGene(entity: TestItemGene): TestItemGene
    suspend fun deleteGeneById(itemGeneId: String)
    fun findGenesByTestCd(tstCd: String): Flow<TestItemGeneResponse>

    // --- TestItemEssentialDoc ---
    suspend fun saveEssentialDoc(entity: TestItemEssentialDoc): TestItemEssentialDoc
    suspend fun findEssentialDocById(itemEstlDocId: String): TestItemEssentialDoc?
    suspend fun deleteEssentialDocById(itemEstlDocId: String)
    fun findEssentialDocsByTstCd(tstCd: String): Flow<TestItemEssentialDocListResponse>
    suspend fun getDetailEssentialDocById(itemEstlDocId: String): TestItemEssentialDocDetailResponse?

    // --- TestItemHst ---
    suspend fun saveTestItemHistory(entity: TestItemHst): TestItemHst
    suspend fun findTestItemHistoryByTstCd(tstCd: String): Flow<TestItemHst>

    // --- TestItemSpecimenHst ---
    suspend fun saveTestItemSpecimenHistory(entity: TestItemSpecimenHst): TestItemSpecimenHst
    suspend fun findTestItemSpecimenHistoryByTstCdAndSpcmCd(tstCd: String, spcmCd: String): Flow<TestItemSpecimenHst>
}
