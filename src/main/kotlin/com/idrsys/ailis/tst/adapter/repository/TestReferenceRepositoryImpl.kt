package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.TestReferenceAutoCompleteParam
import com.idrsys.ailis.tst.application.dto.TestReferenceAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.TestReferenceByGroupParam
import com.idrsys.ailis.tst.application.dto.TestReferenceGroupItemResponse
import com.idrsys.ailis.tst.application.required.TestReferenceRepository
import com.idrsys.ailis.tst.domain.model.TestReference
import com.idrsys.ailis.tst.domain.model.TestReferenceGroup
import com.idrsys.ailis.tst.domain.model.TestReferenceGroupItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstRef
import com.idrsys.ailis.tst.generated.jooq.tables.BbsTstRefGroupItem
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
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
    override suspend fun findAllByRefCateCd(refCateCd: String?): Flow<TestReference> {
        val table = BbsTstRef.BBS_TST_REF
        val query = dslContext.select(table.fields().toList()).from(table)

        if(refCateCd != null) {
            query.where(table.REF_CATE_CD.eq(refCateCd))
        }
        query.where(table.USE_YN.eq(true))
        query.orderBy(table.SORT_ORDER)

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
            .map { row -> toRequestRef(row) }
            .asFlow()
    }

    override fun autoCompleteReferences(searchParam: TestReferenceAutoCompleteParam): Flow<TestReferenceAutoCompleteResponse> {
        val tstRef = BbsTstRef.BBS_TST_REF
        val refCdNm = "%${searchParam.refCdNm}%"

        val condition = tstRef.REF_CD.likeIgnoreCase(refCdNm).or(tstRef.REF_NM.likeIgnoreCase(refCdNm))

        val query = dslContext.select(
            tstRef.REF_CD,
            tstRef.REF_NM,
            tstRef.REF_TYPE,
            tstRef.REF_SIZE,
        )
            .from(tstRef)
            .where(condition)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .map { row, _ ->
                TestReferenceAutoCompleteResponse(
                    refCd = row.get(tstRef.REF_CD.name, String::class.java)!!,
                    refNm = row.get(tstRef.REF_NM.name, String::class.java)!!,
                    refType = row.get(tstRef.REF_TYPE.name, String::class.java)!!,
                    refSize = row.get(tstRef.REF_SIZE.name, Int::class.java)!!
                )
            }
            .all()
            .asFlow()
    }

    private fun toRequestRef(row: Map<String, Any>): TestReference {
        return TestReference(
            refCd = row["ref_cd"] as String,
            refNm = row["ref_nm"] as String,
            refAbbrNm = row["ref_abbr_nm"] as String,
            refEngNm = row["ref_eng_nm"] as String,
            refEngAbbrNm = row["ref_eng_abbr_nm"] as String,
            refType = row["ref_type"] as String,
            refCateCd = row["ref_cate_cd"] as String,
            refMinVal = row["ref_min_val"] as Int,
            refMaxVal = row["ref_max_val"] as Int,
            refSize = row["ref_size"] as Int,
            sortOrder = row["sort_order"] as Int,
            useYn = row["use_yn"] as Boolean,
            rangeChkYn = row["range_chk_yn"] as Boolean,
            creator = row["creator"] as String,
            updater = row["updater"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updateDetime = row["update_detime"] as LocalDateTime,
            dataFormat = row["data_format"] as String,
            dftData = row["dft_data"] as String,
            dftEngData = row["dft_eng_data"] as String,
        )
    }

    override fun getReferenceByRefGroupCd(searchParam: TestReferenceByGroupParam): Flow<TestReference> {
        val tstRef = BbsTstRef.BBS_TST_REF
        val groupItem = BbsTstRefGroupItem.BBS_TST_REF_GROUP_ITEM

        val subQuery = dslContext
            .select(groupItem.REF_CD)
            .from(groupItem)
            .where(groupItem.REF_GROUP_CD.eq(searchParam.refGroupCd))

        var query = dslContext
            .select(tstRef.fields().toList())
            .from(tstRef)
            .where(tstRef.REF_CD.notIn(subQuery))

        searchParam.refCateCd?.let {
            query = query.and(tstRef.REF_CATE_CD.eq(it))
        }

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toRequestRef(row) }
            .asFlow()
    }

    // --- TestReferenceGroup ---
    override suspend fun saveGroup(entity: TestReferenceGroup): TestReferenceGroup = groupDataRepo.save(entity)
    override suspend fun findGroupById(refGroupCd: String): TestReferenceGroup? = groupDataRepo.findById(refGroupCd)
    override suspend fun deleteGroupById(refGroupCd: String) = groupDataRepo.deleteById(refGroupCd)
    override suspend fun findAllGroups(): Flow<TestReferenceGroup> = groupDataRepo.findAll()

    // --- TestReferenceGroupItem ---
    override suspend fun saveGroupItem(entity: TestReferenceGroupItem): TestReferenceGroupItem = groupItemDataRepo.save(entity)
    override suspend fun findGroupItemById(tstRefGroupItemId: String): TestReferenceGroupItem? = groupItemDataRepo.findById(tstRefGroupItemId)
    override suspend fun deleteGroupItemById(tstRefGroupItemId: String) = groupItemDataRepo.deleteById(tstRefGroupItemId)

    override fun findGroupItemsByGroupCd(refGroupCd: String): Flow<TestReferenceGroupItemResponse> {
        val itemTable = BbsTstRefGroupItem.BBS_TST_REF_GROUP_ITEM
        val refTable = BbsTstRef.BBS_TST_REF   // 추가

        val query = dslContext
            .select(
                itemTable.fields().toList() + refTable.REF_NM
            )
            .from(itemTable)
            .leftJoin(refTable)
            .on(itemTable.REF_CD.eq(refTable.REF_CD))
            .where(itemTable.REF_GROUP_CD.eq(refGroupCd))

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
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

    private fun toTestReferenceGroupItem(row: Map<String, Any>): TestReferenceGroupItemResponse {
        return TestReferenceGroupItemResponse(
            tstRefGroupItemId = row["tst_ref_group_item_id"] as String,
            refGroupCd = row["ref_group_cd"] as String,
            refCd = row["ref_cd"] as String,
            refNm = row["ref_nm"] as String,
            sortOrder = (row["sort_order"] as Number).toInt(),
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDetime = row["update_detime"] as LocalDateTime
        )
    }
}
