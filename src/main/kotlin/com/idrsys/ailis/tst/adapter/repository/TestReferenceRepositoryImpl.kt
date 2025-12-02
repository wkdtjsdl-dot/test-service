package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstRefGroupItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : TestReferenceRepository {

    // --- TestReference ---
    override suspend fun save(entity: TestReference): TestReference = refDataRepo.save(entity)
    override suspend fun findById(refCd: String): TestReference? = refDataRepo.findById(refCd)
    override suspend fun findAll(): Flow<TestReference> = refDataRepo.findAll()

    // --- TestReferenceGroup ---
    override suspend fun saveGroup(entity: TestReferenceGroup): TestReferenceGroup = groupDataRepo.save(entity)
    override suspend fun findGroupById(refGroupCd: String): TestReferenceGroup? = groupDataRepo.findById(refGroupCd)
    override suspend fun deleteGroupById(refGroupCd: String) = groupDataRepo.deleteById(refGroupCd)
    override suspend fun findAllGroups(): Flow<TestReferenceGroup> = groupDataRepo.findAll()

    // --- TestReferenceGroupItem ---
    override suspend fun saveGroupItem(entity: TestReferenceGroupItem): TestReferenceGroupItem = groupItemDataRepo.save(entity)
    override suspend fun findGroupItemById(tstRefGroupItemId: String): TestReferenceGroupItem? = groupItemDataRepo.findById(tstRefGroupItemId)
    override suspend fun deleteGroupItemById(tstRefGroupItemId: String) = groupItemDataRepo.deleteById(tstRefGroupItemId)

    override fun findGroupItemsByGroupCd(refGroupCd: String): Flow<TestReferenceGroupItem> {
        val table = BbsTstRefGroupItem.BBS_TST_REF_GROUP_ITEM
        val query = dslContext.select(table.fields().toList())
            .from(table)
            .where(table.REF_GROUP_CD.eq(refGroupCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestReferenceGroupItem(row) }
            .asFlow()
    }

    private fun toTestReferenceGroupItem(row: Map<String, Any>): TestReferenceGroupItem {
        return TestReferenceGroupItem(
            tstRefGroupItemId = row["tst_ref_group_item_id"] as String?,
            refGroupCd = row["ref_group_cd"] as String,
            refCd = row["ref_cd"] as String,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDetime = row["update_detime"] as LocalDateTime
        )
    }
}
