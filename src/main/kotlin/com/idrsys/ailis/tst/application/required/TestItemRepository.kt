package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.TestItemSearchParam
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.domain.model.TestItemRefItem
import com.idrsys.ailis.tst.domain.model.TestItemGene
import kotlinx.coroutines.flow.Flow

interface TestItemRepository {
    // --- TestItem ---
    suspend fun save(entity: TestItem): TestItem
    suspend fun findById(tstCd: String): TestItem?
    fun getItems(searchParam: TestItemSearchParam): Flow<TestItem>

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
    fun findRefItemsByTstCd(tstCd: String): Flow<TestItemRefItem>

    // --- TestItemGene ---
    suspend fun saveGene(entity: TestItemGene): TestItemGene
    suspend fun deleteGeneById(itemGeneId: String)
    fun findGenesByTestCd(tstCd: String): Flow<TestItemGene>
}
