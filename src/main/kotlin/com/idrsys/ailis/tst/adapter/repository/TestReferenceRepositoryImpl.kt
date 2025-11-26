package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstRefGroupItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TestReferenceDataRepository : CoroutineCrudRepository<TestReference, String>

@Repository
interface TestReferenceGroupDataRepository : CoroutineCrudRepository<TestReferenceGroup, String>

@Repository
interface TestReferenceGroupItemDataRepository : CoroutineCrudRepository<TestReferenceGroupItem, String>

@Repository
class TestReferenceRepositoryImpl(
    private val refDataRepo: TestReferenceDataRepository,
    private val groupDataRepo: TestReferenceGroupDataRepository,
    private val groupItemDataRepo: TestReferenceGroupItemDataRepository,
    private val dslContext: DSLContext
) : TestReferenceRepository {

    // --- TestReference ---
    override suspend fun save(entity: TestReference): TestReference = refDataRepo.save(entity)
    override suspend fun findById(id: String): TestReference? = refDataRepo.findById(id)
    override suspend fun deleteById(id: String) = refDataRepo.deleteById(id)
    override suspend fun findAll(): Flow<TestReference> = refDataRepo.findAll()

    // --- TestReferenceGroup ---
    override suspend fun saveGroup(entity: TestReferenceGroup): TestReferenceGroup = groupDataRepo.save(entity)
    override suspend fun findGroupById(id: String): TestReferenceGroup? = groupDataRepo.findById(id)
    override suspend fun deleteGroupById(id: String) = groupDataRepo.deleteById(id)
    override suspend fun findAllGroups(): Flow<TestReferenceGroup> = groupDataRepo.findAll()

    // --- TestReferenceGroupItem ---
    override suspend fun saveGroupItem(entity: TestReferenceGroupItem): TestReferenceGroupItem = groupItemDataRepo.save(entity)
    override suspend fun findGroupItemById(id: String): TestReferenceGroupItem? = groupItemDataRepo.findById(id)
    override suspend fun deleteGroupItemById(id: String) = groupItemDataRepo.deleteById(id)
    
    override suspend fun findGroupItemsByGroupCd(groupCd: String): Flow<TestReferenceGroupItem> {
        // Using jOOQ for custom query example, though simple findBy could be done with Spring Data too.
        // Keeping consistent with established pattern of using jOOQ for non-PK lookups in this refactoring.
        val table = BbsTstRefGroupItem.BBS_TST_REF_GROUP_ITEM
        return dslContext.select(table.fields().toList())
            .from(table)
            .where(table.REF_GROUP_CD.eq(groupCd))
            .asFlow()
            .map { r: org.jooq.Record -> r.into(TestReferenceGroupItem::class.java) }
    }
}
