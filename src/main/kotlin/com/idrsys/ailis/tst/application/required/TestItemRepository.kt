package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import kotlinx.coroutines.flow.Flow

interface TestItemRepository {
    // --- TestItem ---
    suspend fun save(entity: TestItem): TestItem
    suspend fun findById(id: String): TestItem?
    suspend fun deleteById(id: String)
    suspend fun findAll(): Flow<TestItem>
    suspend fun findByLargeCateCd(code: String): Flow<TestItem>

    // --- StandardCharge ---
    suspend fun saveCharge(entity: StandardCharge): StandardCharge
    suspend fun findChargeById(id: String): StandardCharge?
    suspend fun deleteChargeById(id: String)
    suspend fun findChargesByTestCd(tstCd: String): Flow<StandardCharge>

    // --- TestItemSpecimen ---
    suspend fun saveSpecimen(entity: TestItemSpecimen): TestItemSpecimen
    suspend fun findSpecimenById(id: String): TestItemSpecimen?
    suspend fun deleteSpecimenById(id: String)
    suspend fun findSpecimensByTestCd(tstCd: String): Flow<TestItemSpecimen>
}
