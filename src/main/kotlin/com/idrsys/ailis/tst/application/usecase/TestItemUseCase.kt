package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.*
import kotlinx.coroutines.flow.Flow

interface TestItemUseCase {
    // --- TestItem ---
    suspend fun registerItem(request: TestItemRegisterRequest, adminId: String): TestItemResponse
    suspend fun getItem(id: String): TestItemResponse
    suspend fun updateItem(id: String, request: TestItemUpdateRequest, adminId: String): TestItemResponse
    suspend fun deleteItem(id: String, adminId: String)
    suspend fun getAllItems(): Flow<TestItemResponse>
    suspend fun getItemsByLargeCate(code: String): Flow<TestItemResponse>

    // --- StandardCharge ---
    suspend fun registerCharge(request: StandardChargeRegisterRequest, adminId: String): StandardChargeResponse
    suspend fun getCharge(id: String): StandardChargeResponse
    suspend fun deleteCharge(id: String, adminId: String)
    suspend fun getChargesByTest(tstCd: String): Flow<StandardChargeResponse>

    // --- TestItemSpecimen ---
    suspend fun registerSpecimen(request: TestItemSpecimenRegisterRequest, adminId: String): TestItemSpecimenResponse
    suspend fun getSpecimen(id: String): TestItemSpecimenResponse
    suspend fun deleteSpecimen(id: String, adminId: String)
    suspend fun getSpecimensByTest(tstCd: String): Flow<TestItemSpecimenResponse>
}
