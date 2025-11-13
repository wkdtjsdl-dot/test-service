package com.idrsys.ailis.sales.adapter.repository.gcgnSalsPicInfo

import com.idrsys.ailis.sales.application.dto.query.GcgnSalsPicInfoQuery
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoCustomRepository
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_GCGN_SALS_PIC_INFO
import com.idrsys.ailis.sales.shared.mapper.GcgnSalsPicInfoMapper
import com.idrsys.ailis.sales.adapter.persistence.mapper.toGcgnSalsPicInfo
import com.idrsys.ailis.sales.adapter.persistence.mapper.toGcgnSalsPicInfoQuery
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalaPicInfoAutoSearchParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.jooq.impl.DSL.falseCondition
import org.jooq.impl.DSL.trueCondition
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class GcgnSalsPicInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
    private val gcgnSalsPicInfoMapper: GcgnSalsPicInfoMapper,
) : GcgnSalsPicInfoCustomRepository {

    override fun findGcgnSalsPicInfos(searchParam: GcgnSalsPicInfoSearchParam, pageable: Pageable?): Flow<GcgnSalsPicInfoQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_GCGN_SALS_PIC_INFO.asterisk()
        )
            .from(SCS_GCGN_SALS_PIC_INFO)
            .where(conditions)
            .orderBy(SCS_GCGN_SALS_PIC_INFO.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toGcgnSalsPicInfoQuery() }.all().asFlow()
    }

    override suspend fun countGcgnSalsPicInfos(searchParam: GcgnSalsPicInfoSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_GCGN_SALS_PIC_INFO)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findGcgnSalsPicInfoById(gcgnSalsPicInfoId: Long): GcgnSalsPicInfoQuery? {
        val query = dslContext.select(
            SCS_GCGN_SALS_PIC_INFO.asterisk()
        )
            .from(SCS_GCGN_SALS_PIC_INFO)
            .where(SCS_GCGN_SALS_PIC_INFO.GCGN_SALS_PIC_INFO_ID.eq(gcgnSalsPicInfoId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toGcgnSalsPicInfoQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): GcgnSalsPicInfo? {
        val query = dslContext.select(SCS_GCGN_SALS_PIC_INFO.asterisk())
            .from(SCS_GCGN_SALS_PIC_INFO)
            .where(SCS_GCGN_SALS_PIC_INFO.GCGN_SALS_PIC_INFO_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toGcgnSalsPicInfo() }.one().awaitSingleOrNull()
    }
    override fun findEmpUserIdsForAutoComplete(searchParam: GcgnSalaPicInfoAutoSearchParam, includeUserIds: Collection<String>): Flow<String> {
        val keyword = searchParam.empUserIdNm?.trim()?.takeIf { it.isNotEmpty() }

        var cond: Condition = trueCondition()

        if (keyword != null) {
            cond = cond.and(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.likeIgnoreCase("%$keyword%")
                    .or(
                        if (includeUserIds.isEmpty()) falseCondition()
                        else SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.`in`(includeUserIds)
                    )
            )
        }
        val query = dslContext
            .selectDistinct(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID)
            .from(SCS_GCGN_SALS_PIC_INFO)
            .where(cond)
            .orderBy(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.desc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.get("emp_user_id", String::class.java)!! }
            .all()
            .asFlow()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: GcgnSalsPicInfoSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID.eq(it)
        }
        searchParam.salsTeamCd?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_GCGN_SALS_PIC_INFO.SALS_TEAM_CD.eq(it)
        }
        searchParam.empUserId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.eq(it)
        }
        return conds
    }
}
