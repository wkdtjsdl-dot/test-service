package com.idrsys.ailis.sales.adapter.repository.ifCustInfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.toIfCustInfo
import com.idrsys.ailis.sales.adapter.persistence.mapper.toIfCustInfoQuery
import com.idrsys.ailis.sales.application.dto.query.IfCustInfoQuery
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoSearchParam
import com.idrsys.ailis.sales.application.required.repository.ifCustInfo.IfCustInfoCustomRepository
import com.idrsys.ailis.sales.domain.model.IfCustInfo
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_IF_CUST_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class IfCustInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : IfCustInfoCustomRepository {

    override fun findIfCustInfos(
        searchParam: IfCustInfoSearchParam,
        pageable: Pageable?
    ): Flow<IfCustInfoQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_IF_CUST_INFO.asterisk(),
            SCS_CUST_MST.CUST_NM.`as`("cust_nm")
        )
            .from(SCS_IF_CUST_INFO)
            .leftJoin(SCS_CUST_MST).on(SCS_IF_CUST_INFO.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(conditions)
            .orderBy(SCS_IF_CUST_INFO.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfCustInfoQuery() }.all().asFlow()
    }

    override suspend fun countIfCustInfos(searchParam: IfCustInfoSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_IF_CUST_INFO)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findIfCustInfoById(ifCustInfoId: String): IfCustInfoQuery? {
        val query = dslContext.select(
            SCS_IF_CUST_INFO.asterisk(),
            SCS_CUST_MST.CUST_NM.`as`("cust_nm")
        )
            .from(SCS_IF_CUST_INFO)
            .leftJoin(SCS_CUST_MST).on(SCS_IF_CUST_INFO.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(SCS_IF_CUST_INFO.IF_CUST_INFO_ID.eq(ifCustInfoId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfCustInfoQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: String): IfCustInfo? {
        val query = dslContext.select(SCS_IF_CUST_INFO.asterisk())
            .from(SCS_IF_CUST_INFO)
            .where(SCS_IF_CUST_INFO.IF_CUST_INFO_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfCustInfo() }.one().awaitSingleOrNull()
    }

    override suspend fun findByCustMstId(custMstId: String): IfCustInfo? {
        val query = dslContext.select(SCS_IF_CUST_INFO.asterisk())
            .from(SCS_IF_CUST_INFO)
            .where(SCS_IF_CUST_INFO.CUST_MST_ID.eq(custMstId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toIfCustInfo() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: IfCustInfoSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_IF_CUST_INFO.CUST_MST_ID.eq(it)
        }
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_IF_CUST_INFO.CUST_CD.eq(it)
        }

        return conds
    }
}
