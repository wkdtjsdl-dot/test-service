package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestItemUseCase {
    // --- TestItem ---
    suspend fun registerItem(request: TestItemRegisterRequest, adminId: String): TestItemResponse
    suspend fun getItem(tstCd: String): TestItemResponse
    suspend fun updateItem(tstCd: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse
    fun getItems(searchParam: TestItemSearchParam): Flow<TestItemResponse>

    // --- StandardCharge ---
    suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse
    suspend fun getCharge(id: String): StandardChargeResponse
    suspend fun deleteCharge(id: String, adminId: String)
    suspend fun getChargesByTest(tstCd: String): Flow<StandardChargeResponse>

    // --- TestItemSpecimen ---
    suspend fun registerSpecimen(request: TestItemSpecimenRegisterRequest, adminId: String): TestItemSpecimenResponse
    suspend fun getSpecimen(spcmId: String): TestItemSpecimenResponse
    suspend fun deleteSpecimen(id: String, adminId: String)
    fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenResponse>

    // --- TestItemRefItem ---
    suspend fun registerRefItem(request: TestItemRefItemRegisterRequest, adminId: String): TestItemRefItemResponse
    suspend fun getRefItem(refItemId: String): TestItemRefItemResponse
    suspend fun updateRefItem(refItemId: String, request: TestItemRefItemUpdateRequest, adminId: String): TestItemRefItemResponse
    suspend fun deleteRefItem(refItemId: String, adminId: String)
    fun getRefItemsByTstCd(tstCd: String): Flow<TestItemRefItemResponse>
}
