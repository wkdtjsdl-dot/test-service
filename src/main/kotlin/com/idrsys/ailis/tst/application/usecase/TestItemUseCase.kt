package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestItemUseCase {
    // --- TestItem ---
    suspend fun registerItem(request: TestItemRegisterRequest, adminId: String): TestItemResponse
    suspend fun getItem(tstCd: String): TestItemResponse
    suspend fun updateItem(tstCd: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse
    fun getItems(searchParam: TestItemSearchParam): Flow<TestItemResponse>
    fun getItemsSimple(searchParam: TestItemSearchParam): Flow<TestItemSimpleResponse>
    fun autoCompleteItems(searchParam: TestItemAutoCompleteParam): Flow<TestItemSimpleResponse>
    suspend fun findSimpleItemByTstCd(tstCds: List<String>): Flow<TestItemSimpleResponse>
    suspend fun findSimpleItemAll(searchParam: TestItemAllSearchParam): Flow<TestItemSimpleResponse>

    // --- StandardCharge ---
    suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse
    suspend fun getCharge(id: String): StandardChargeResponse
    suspend fun deleteCharge(id: String, adminId: String)
    suspend fun updateCharge(id: String, request: StandardChargeUpdateRequest, adminId: String): StandardChargeResponse
    suspend fun getCurrentStandardChargeByTest(tstCd: String): StandardChargeResponse?
    suspend fun getChargesByTest(tstCd: String, sort: String? = null): Flow<StandardChargeResponse>

    // --- TestItemSpecimen ---
    suspend fun registerSpecimen(request: TestItemSpecimenRegisterRequest, adminId: String): TestItemSpecimenDetailResponse
    suspend fun getSpecimen(spcmId: String): TestItemSpecimenDetailResponse
    suspend fun updateSpecimen(spcmId: String, request: TestItemSpecimenUpdateRequest, adminId: String): TestItemSpecimenDetailResponse
    suspend fun deleteSpecimen(id: String, adminId: String)
    fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenListResponse>
    suspend fun getSpecimensByTstCds(tstCds: List<String>): Flow<TestItemSpecimensResponse>

    // --- TestItemRefItem ---
    suspend fun registerRefItem(request: TestItemRefItemRegisterRequest, adminId: String): TestItemRefItemResponse
    suspend fun getRefItem(refItemId: String): TestItemRefDetailResponse
    suspend fun updateRefItem(refItemId: String, request: TestItemRefItemUpdateRequest, adminId: String): TestItemRefItemResponse
    suspend fun deleteRefItem(refItemId: String, adminId: String)
    fun getRefItemsByTstCd(searchParam: TestItemRefRequest): Flow<TestItemRefResponse>
    suspend fun getRefItemsByTstCds(tstCds: List<String>): Flow<TestItemRefItemsResponse>
    suspend fun getRefItemsDetailByTstCds(tstCds: List<String>): Flow<TestItemRefDetailItemsResponse>

    // --- TestGene ---
     fun getGenes(request: TestGeneRequest): Flow<TestGeneResponse>
    // --- TestItemGene ---
    suspend fun registerGene(request: TestItemGeneRegisterRequest, adminId: String): TestItemGeneResponse
    suspend fun deleteGene(itemGeneId: String, adminId: String)
    fun getGenesByTest(tstCd: String): Flow<TestItemGeneResponse>

    // --- TestItemEssentialDoc ---
    suspend fun registerEssentialDoc(request: TestItemEssentialDocRegisterRequest, adminId: String): TestItemEssentialDocResponse
    suspend fun getEssentialDoc(itemEstlDocId: String): TestItemEssentialDocResponse
    suspend fun updateEssentialDoc(itemEstlDocId: String, request: TestItemEssentialDocUpdateRequest, adminId: String): TestItemEssentialDocResponse
    suspend fun deleteEssentialDoc(itemEstlDocId: String, adminId: String)
    fun getEssentialDocsByTest(tstCd: String): Flow<TestItemEssentialDocListResponse>
    suspend fun getDetailEssentialDocById(itemEstlDocId: String): TestItemEssentialDocDetailResponse?

    // --- TestItemSub ---
    fun getItemSubByTest(tstCd: String): Flow<TestItemSubResponse>
    suspend fun registerItemSub(request: TestItemSubRegisterRequest, adminId: String): TestItemSubResponse
    suspend fun updateItemSub(itemSubId: String, request: TestItemSubUpdateRequest, adminId: String): TestItemSubResponse

    // --- TestItemHst ---
    suspend fun getTestItemHistoryLogList(searchParam: TestItemLogsSearchParam): List<TestItemLogsResponse>

    // --- TestItemSpecimenHst ---
    suspend fun getTestItemSpecimenHistoryLogList(searchParam: TestItemSpecimenLogsSearchParam): List<TestItemSpecimenLogsResponse>
}
