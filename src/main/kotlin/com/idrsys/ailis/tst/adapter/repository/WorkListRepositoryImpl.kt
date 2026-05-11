package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.WorkListAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.request.WorkListAutoCompleteSearchParam
import com.idrsys.ailis.tst.application.dto.request.WorkListSearchParam
import com.idrsys.ailis.tst.application.required.repository.WorkListRepository
import com.idrsys.ailis.tst.domain.model.WorkList
import com.idrsys.ailis.tst.domain.model.WorkListItem
import com.idrsys.ailis.tst.generated.jooq.tables.BbsSpcm
import com.idrsys.ailis.tst.generated.jooq.tables.BbsWrklist
import com.idrsys.ailis.tst.generated.jooq.tables.BbsWrklistItm
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.impl.DSL
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface WorkListDataRepository : CoroutineCrudRepository<WorkList, String>

@Repository
interface WorkListItemDataRepository : CoroutineCrudRepository<WorkListItem, String>

@Repository
class WorkListRepositoryImpl(
    private val workListDataRepository: WorkListDataRepository,
    private val workListItemDataRepository: WorkListItemDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : WorkListRepository {

    override suspend fun save(workList: WorkList): WorkList = workListDataRepository.save(workList)

    override suspend fun findById(wrklistCd: String): WorkList? = workListDataRepository.findById(wrklistCd)

    override fun findBySearch(searchParam: WorkListSearchParam): Flow<WorkList> {
        val table = BbsWrklist.BBS_WRKLIST
        var condition: Condition = DSL.noCondition()

        searchParam.wrklistCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(table.WRKLIST_CD.eq(it))
        }
        searchParam.wrklistCdLike?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(table.WRKLIST_CD.like("%$it%"))
        }
        searchParam.wrklistNm?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(table.WRKLIST_NM.like("%$it%"))
        }
        searchParam.useYn?.let {
            condition = condition.and(table.USE_YN.eq(it))
        }

        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(condition)
            .orderBy(table.WRKLIST_CD.asc())

        return bind(query)
            .fetch()
            .all()
            .map { row -> toWorkList(row) }
            .asFlow()
    }

    override suspend fun saveItem(workListItem: WorkListItem): WorkListItem = workListItemDataRepository.save(workListItem)

    override suspend fun findItemByIdAndWrklistCd(wrklistItmId: String, wrklistCd: String): WorkListItem? {
        val table = BbsWrklistItm.BBS_WRKLIST_ITM
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.WRKLIST_ITM_ID.eq(wrklistItmId))
            .and(table.WRKLIST_CD.eq(wrklistCd))

        return bind(query)
            .fetch()
            .one()
            .map { row -> toWorkListItem(row) }
            .awaitSingleOrNull()
    }

    override suspend fun findItemByWrklistCdAndTstCd(wrklistCd: String, tstCd: String): WorkListItem? {
        val table = BbsWrklistItm.BBS_WRKLIST_ITM
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.WRKLIST_CD.eq(wrklistCd))
            .and(table.TST_CD.eq(tstCd))

        return bind(query)
            .fetch()
            .one()
            .map { row -> toWorkListItem(row) }
            .awaitSingleOrNull()
    }

    override suspend fun deleteItemById(wrklistItmId: String) {
        workListItemDataRepository.deleteById(wrklistItmId)
    }

    override fun autoCompleteWorkLists(searchParam: WorkListAutoCompleteSearchParam): Flow<WorkListAutoCompleteResponse> {
        val table = BbsWrklist.BBS_WRKLIST
        var condition: Condition = DSL.noCondition()

        searchParam.wrklstCd?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(table.WRKLIST_CD.eq(it))
        }
        searchParam.wrklstCdNm?.takeIf { it.isNotBlank() }?.let {
            condition = condition.and(
                table.WRKLIST_CD.containsIgnoreCase(it).or(table.WRKLIST_NM.containsIgnoreCase(it))
            )
        }

        val query = dslContext
            .select(table.WRKLIST_CD, table.WRKLIST_NM)
            .from(table)
            .where(condition)
            .orderBy(table.WRKLIST_CD.asc())

        return bind(query)
            .fetch()
            .all()
            .map { row ->
                WorkListAutoCompleteResponse(
                    wrklistCd = row["wrklist_cd"] as String,
                    wrklistNm = row["wrklist_nm"] as? String
                )
            }
            .asFlow()
    }

    override fun getSimpleList(useYn: Boolean?): Flow<WorkListAutoCompleteResponse> {
        val table = BbsWrklist.BBS_WRKLIST
        var condition: Condition = DSL.noCondition()

        useYn?.let { condition = condition.and(table.USE_YN.eq(it)) }

        val query = dslContext
            .select(table.WRKLIST_CD, table.WRKLIST_NM)
            .from(table)
            .where(condition)
            .orderBy(table.WRKLIST_CD.asc())

        return bind(query)
            .fetch()
            .all()
            .map { row ->
                WorkListAutoCompleteResponse(
                    wrklistCd = row["wrklist_cd"] as String,
                    wrklistNm = row["wrklist_nm"] as? String
                )
            }
            .asFlow()
    }

    override fun findItemsByWrklistCd(wrklistCd: String): Flow<WorkListItemDetailResponse> {
        return findItemsByWrklistCds(listOf(wrklistCd))
    }

    override fun findItemsByWrklistCds(wrklistCds: List<String>): Flow<WorkListItemDetailResponse> {
        val table = BbsWrklistItm.BBS_WRKLIST_ITM
        val testTable = BtsItem.BTS_ITEM
        val specimenTable = BbsSpcm.BBS_SPCM

        val query = dslContext
            .select(
                table.fields().toList() +
                    testTable.TST_NM.`as`("tst_nm") +
                    specimenTable.SPCM_NM.`as`("spcm_nm")
            )
            .from(table)
            .leftJoin(testTable)
            .on(table.TST_CD.eq(testTable.TST_CD))
            .leftJoin(specimenTable)
            .on(table.SPCM_CD.eq(specimenTable.SPCM_CD))
            .where(table.WRKLIST_CD.`in`(wrklistCds))
            .orderBy(table.TST_CD.asc())

        return bind(query)
            .fetch()
            .all()
            .map { row -> toWorkListItemDetailResponse(row) }
            .asFlow()
    }

    private fun bind(query: Query): DatabaseClient.GenericExecuteSpec {
        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = if (value != null) {
                executeSpec.bind(index, value)
            } else {
                executeSpec.bindNull(index, String::class.java)
            }
        }
        return executeSpec
    }

    private fun toWorkList(row: Map<String, Any>): WorkList {
        return WorkList(
            wrklistCd = row["wrklist_cd"] as String,
            useYn = row["use_yn"] as Boolean,
            startDt = row["start_dt"] as LocalDate,
            endDt = row["end_dt"] as LocalDate,
            wrklistNm = row["wrklist_nm"] as? String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }

    private fun toWorkListItem(row: Map<String, Any>): WorkListItem {
        return WorkListItem(
            wrklistItmId = row["wrklist_itm_id"] as String,
            wrklistCd = row["wrklist_cd"] as String,
            tstCd = row["tst_cd"] as String,
            spcmCd = row["spcm_cd"] as? String,
            tstOption = row["tst_option"] as? String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }

    private fun toWorkListItemDetailResponse(row: Map<String, Any>): WorkListItemDetailResponse {
        return WorkListItemDetailResponse(
            wrklistItmId = row["wrklist_itm_id"] as String,
            wrklistCd = row["wrklist_cd"] as String,
            tstCd = row["tst_cd"] as String,
            tstNm = row["tst_nm"] as? String,
            spcmCd = row["spcm_cd"] as? String,
            spcmNm = row["spcm_nm"] as? String,
            tstOption = row["tst_option"] as? String,
            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
