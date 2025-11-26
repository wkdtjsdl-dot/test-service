package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.TestItemRepository
import com.idrsys.ailis.tst.domain.model.StandardCharge
import com.idrsys.ailis.tst.domain.model.TestItem
import com.idrsys.ailis.tst.domain.model.TestItemSpecimen
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsStndCharge
import com.idrsys.ailis.tst.generated.jooq.tables.BtsSpcm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TestItemDataRepository : CoroutineCrudRepository<TestItem, String>

@Repository
interface StandardChargeDataRepository : CoroutineCrudRepository<StandardCharge, String>

@Repository
interface TestItemSpecimenDataRepository : CoroutineCrudRepository<TestItemSpecimen, String>

@Repository
class TestItemRepositoryImpl(
    private val itemDataRepo: TestItemDataRepository,
    private val chargeDataRepo: StandardChargeDataRepository,
    private val specimenDataRepo: TestItemSpecimenDataRepository,
    private val dslContext: DSLContext
) : TestItemRepository {

    // --- TestItem ---
    override suspend fun save(entity: TestItem): TestItem = itemDataRepo.save(entity)
    override suspend fun findById(id: String): TestItem? = itemDataRepo.findById(id)
    override suspend fun deleteById(id: String) = itemDataRepo.deleteById(id)
    override suspend fun findAll(): Flow<TestItem> = itemDataRepo.findAll()

    override suspend fun findByLargeCateCd(code: String): Flow<TestItem> {
        val table = BtsItem.BTS_ITEM
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.TST_LARGE_CATE_CD.eq(code))
            .asFlow()
            .map { r: Record -> r.into(TestItem::class.java) }
    }

    // --- StandardCharge ---
    override suspend fun saveCharge(entity: StandardCharge): StandardCharge = chargeDataRepo.save(entity)
    override suspend fun findChargeById(id: String): StandardCharge? = chargeDataRepo.findById(id)
    override suspend fun deleteChargeById(id: String) = chargeDataRepo.deleteById(id)

    override suspend fun findChargesByTestCd(tstCd: String): Flow<StandardCharge> {
        val table = BtsStndCharge.BTS_STND_CHARGE
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))
            .asFlow()
            .map { r: Record -> r.into(StandardCharge::class.java) }
    }

    // --- TestItemSpecimen ---
    override suspend fun saveSpecimen(entity: TestItemSpecimen): TestItemSpecimen = specimenDataRepo.save(entity)
    override suspend fun findSpecimenById(id: String): TestItemSpecimen? = specimenDataRepo.findById(id)
    override suspend fun deleteSpecimenById(id: String) = specimenDataRepo.deleteById(id)

    override suspend fun findSpecimensByTestCd(tstCd: String): Flow<TestItemSpecimen> {
        val table = BtsSpcm.BTS_SPCM
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))
            .asFlow()
            .map { r: Record -> r.into(TestItemSpecimen::class.java) }
    }
}
