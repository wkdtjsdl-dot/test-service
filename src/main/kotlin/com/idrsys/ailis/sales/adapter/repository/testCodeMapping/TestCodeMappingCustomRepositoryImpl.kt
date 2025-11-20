package com.idrsys.ailis.sales.adapter.repository.testCodeMapping

import com.idrsys.ailis.sales.adapter.persistence.mapper.toTestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.required.repository.testCodeMapping.TestCodeMappingCustomRepository
import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_TST_CD_MPG
import com.idrsys.ailis.sales.shared.mapper.TestCodeMappingMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TestCodeMappingCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
): TestCodeMappingCustomRepository {

    override fun findTestCodeMappings(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Flow<TestCodeMappingQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_CUST_TST_CD_MPG.asterisk(),
            SCS_CUST_MST.CUST_NM
        ).from(SCS_CUST_TST_CD_MPG)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_TST_CD_MPG.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .orderBy(SCS_CUST_TST_CD_MPG.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toTestCodeMappingQuery() }.all().asFlow()
    }

    override suspend fun searchTestCodeMappingList(searchParam: TestCodeMappingSearchParam): Flow<CustTestCodeMapping> =
        selectTestCodeMappings(searchParam)

    private fun selectTestCodeMappings(searchParam: TestCodeMappingSearchParam, pageable: Pageable? = null): Flow<CustTestCodeMapping> {
        val query = if (pageable != null) {
            makeSelectQuery(searchParam)
                .limit(DSL.param("pageSize", pageable.pageSize))
                .offset(DSL.param("pageOffset", pageable.offset.toInt()))
        } else {
            makeSelectQuery(searchParam)
        }

        var bindQuery = databaseClient.sql(query.sql)

        // 파라미터 바인딩
        query.bindValues.forEachIndexed { i, v -> bindQuery = bindQuery.bind(i, v) }

        if (pageable != null) {
            bindQuery = bindQuery
                .bind("pageSize", pageable.pageSize)
                .bind("pageOffset", pageable.offset.toInt())
        }

        return bindQuery
            .fetch()
            .all()
            .map { row ->
                CustTestCodeMapping(
                    custTstCdMpgId = row["cust_tst_cd_mpg_id"] as String,
                    custMstId = row["cust_mst_id"] as String?,
                    custCd = row["cust_cd"] as String,
                    custTstCd = row["cust_tst_cd"] as String,
                    custSubTstCd = row["cust_sub_tst_cd"] as String?,
                    custTstNm = row["cust_tst_nm"] as String?,
                    tstCd = row["tst_cd"] as String?,
                    tstNm = row["tst_nm"] as String?,
                    creator = row["creator"] as String,
                    createDtime = row["create_dtime"] as LocalDateTime,
                    updater = row["updater"] as String,
                    updateDtime = row["update_dtime"] as LocalDateTime
                )
            }
            .asFlow()
    }

    private fun makeSelectQuery(searchParam: TestCodeMappingSearchParam): SelectSeekStep1<Record12<String, String, String, String, String, String, String, String, String, LocalDateTime, String, LocalDateTime>, LocalDateTime> =
        dslContext
            .select(
                SCS_CUST_TST_CD_MPG.CUST_TST_CD_MPG_ID,
                SCS_CUST_TST_CD_MPG.CUST_MST_ID,
                SCS_CUST_TST_CD_MPG.CUST_CD,
                SCS_CUST_TST_CD_MPG.CUST_TST_CD,
                SCS_CUST_TST_CD_MPG.CUST_SUB_TST_CD,
                SCS_CUST_TST_CD_MPG.CUST_TST_NM,
                SCS_CUST_TST_CD_MPG.TST_CD,
                SCS_CUST_TST_CD_MPG.TST_NM,
                SCS_CUST_TST_CD_MPG.CREATOR,
                SCS_CUST_TST_CD_MPG.CREATE_DTIME,
                SCS_CUST_TST_CD_MPG.UPDATER,
                SCS_CUST_TST_CD_MPG.UPDATE_DTIME

            )
            .from(SCS_CUST_TST_CD_MPG)
            .where(buildConditions(searchParam))
            .orderBy(SCS_CUST_TST_CD_MPG.CREATE_DTIME.asc())

    override suspend fun countTestCodeMapping(searchParam: TestCodeMappingSearchParam): Long {
        val conditions = buildConditions(searchParam)

        val countQuery = dslContext.selectCount()
        val query = countQuery.from(SCS_CUST_TST_CD_MPG)

        val finalQuery = query.where(conditions)

        var sql = databaseClient.sql(finalQuery.sql)
        finalQuery.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)?.toLong() ?: 0L }.one().awaitSingleOrNull() ?: 0L
    }

    private fun buildConditions(searchParam: TestCodeMappingSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_TST_CD_MPG.CUST_CD.eq(it) }
        return conds
    }

    private fun applyPaging(q: SelectLimitStep<out org.jooq.Record>, pageable: Pageable): Query {
        if(pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }
}