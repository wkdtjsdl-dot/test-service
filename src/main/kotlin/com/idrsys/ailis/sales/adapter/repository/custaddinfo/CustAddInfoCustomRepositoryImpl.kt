package com.idrsys.ailis.sales.adapter.repository.custaddinfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustAddInfo
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoCustomRepository
import com.idrsys.ailis.sales.domain.model.CustAddInfo
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_ADD_INFO
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
class CustAddInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustAddInfoCustomRepository {

    override fun findCustAddInfos(searchParam: CustAddInfoSearchParam, pageable: Pageable?): Flow<CustAddInfoQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectFrom(SCS_CUST_ADD_INFO)
            .where(conditions)
            .orderBy(SCS_CUST_ADD_INFO.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfoQuery() }.all().asFlow()
    }

    override suspend fun countCustAddInfos(searchParam: CustAddInfoSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_ADD_INFO)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findCustAddInfoById(custAddInfoId: Long): CustAddInfoQuery? {
        val query = dslContext.selectFrom(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_ADD_INFO_ID.eq(custAddInfoId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfoQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): CustAddInfo? {
        val query = dslContext.select(SCS_CUST_ADD_INFO.asterisk())
            .from(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_ADD_INFO_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfo() }.one().awaitSingleOrNull()
    }

    override fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoQuery> {
        val query = dslContext.selectFrom(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_MST_ID.eq(searchParam.custMstId))
            .and(SCS_CUST_ADD_INFO.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfoQuery() }.all().asFlow()
    }

    override suspend fun findByCustMstId(custMstId: String): CustAddInfoQuery? {
        val query = dslContext.selectFrom(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_MST_ID.eq(custMstId))
            .and(SCS_CUST_ADD_INFO.USE_YN.isTrue)
            .limit(1) // Assuming only one additional info per customer master ID

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfoQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainByCustMstId(custMstId: String): CustAddInfo? {
        val query = dslContext.select(SCS_CUST_ADD_INFO.asterisk())
            .from(SCS_CUST_ADD_INFO)
            .where(SCS_CUST_ADD_INFO.CUST_MST_ID.eq(custMstId))
            .and(SCS_CUST_ADD_INFO.USE_YN.isTrue)
            .limit(1)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustAddInfo() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustAddInfoSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_ADD_INFO.CUST_MST_ID.eq(it)
        }
        conds += SCS_CUST_ADD_INFO.USE_YN.isTrue
        return conds
    }
}
