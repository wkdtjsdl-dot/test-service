package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.*
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
import com.idrsys.ailis.tst.domain.model.TestItemSub
import com.idrsys.ailis.tst.domain.model.TestItemSubHst
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface TestItemRepository {
    // --- TestItem ---
    suspend fun save(entity: TestItem): TestItem
    suspend fun findById(tstCd: String): TestItem?
    suspend fun findAll(searchParam: TestItemAllSearchParam): Flow<TestItem>
    fun getItemList(searchParam: TestItemSearchParam): Flow<TestItemListRow>
    fun findUnspecifiedDeptItems(searchParam: UnspecifiedDepartmentTestItemSearchParam): Flow<TestItem>
    fun findSimpleItems(searchParam: TestItemSearchParam): Flow<TestItemSimpleResponse>
    fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemSimpleResponse>
    fun findSimpleItemByTstCd(tstCds: List<String>): Flow<TestItemSimpleResponse>

    // --- StandardCharge ---
    suspend fun saveCharge(entity: StandardCharge): StandardCharge
    suspend fun findChargeById(id: String): StandardCharge?
    suspend fun deleteChargeById(id: String)
    suspend fun findChargesByTestCd(tstCd: String, sort: String? = null): Flow<StandardCharge>
    suspend fun getEqualDate(entity: StandardCharge): Flow<StandardCharge>
    suspend fun findLatestChargeByTestCdAndDate(tstCd: String, currentDate: LocalDate): StandardCharge?

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
    suspend fun findRefItemsDetailByTstCds(tstCds: List<String>): Flow<TestItemRefDetailItemsResponse>

    // --- TestGene ---
    suspend fun countGenes(keyword: String?, tstCd: String): Long
    suspend fun findGenesPaged(keyword: String?, tstCd: String, pageable: Pageable): List<TestGeneResponse>
    suspend fun findValidGeneCds(geneCds: List<String>): List<String>
    // --- TestItemGene ---
    suspend fun saveGene(entity: TestItemGene): TestItemGene
    suspend fun deleteGeneById(itemGeneId: String)
    suspend fun countGenesByTest(tstCd: String, keyword: String?): Long
    suspend fun findGenesByTestPaged(tstCd: String, keyword: String?, pageable: Pageable): List<TestItemGeneResponse>
    suspend fun findExistingItemGeneCds(tstCd: String, geneCds: List<String>): List<String>

    // --- TestItemEssentialDoc ---
    suspend fun saveEssentialDoc(entity: TestItemEssentialDoc): TestItemEssentialDoc
    suspend fun findEssentialDocById(itemEstlDocId: String): TestItemEssentialDoc?
    suspend fun deleteEssentialDocById(itemEstlDocId: String)
    fun findEssentialDocsByTstCd(tstCd: String): Flow<TestItemEssentialDocListResponse>
    suspend fun getDetailEssentialDocById(itemEstlDocId: String): TestItemEssentialDocDetailResponse?

    // --- TestItemSub ---
    fun findItemSubsByTstCd(tstCd: String): Flow<TestItemSub>
    suspend fun saveItemSub(entity: TestItemSub): TestItemSub
    suspend fun findItemSubById(itemSubId: String): TestItemSub?
    suspend fun saveItemSubHistory(entity: TestItemSubHst): TestItemSubHst

    // --- TestItemHst ---
    suspend fun saveTestItemHistory(entity: TestItemHst): TestItemHst
    suspend fun findTestItemHistoryByTstCd(tstCd: String): Flow<TestItemHst>

    // --- TestItemSpecimenHst ---
    suspend fun saveTestItemSpecimenHistory(entity: TestItemSpecimenHst): TestItemSpecimenHst
    suspend fun findTestItemSpecimenHistoryByTstCdAndSpcmCd(tstCd: String, spcmCd: String): Flow<TestItemSpecimenHst>
}
