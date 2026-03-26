package com.idrsys.ailis.sales.adapter.repository.custreqposststitem

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.query.CustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_REQ_POSS_TST_ITEM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CustReqPossTstItemCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustReqPossTstItemCustomRepository {

    override fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemQuery> {
        val query = dslContext.selectFrom(SCS_CUST_REQ_POSS_TST_ITEM)
            .where(SCS_CUST_REQ_POSS_TST_ITEM.CUST_MST_ID.eq(searchParam.custMstId))
            .orderBy(SCS_CUST_REQ_POSS_TST_ITEM.TST_CD.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustReqPossTstItemQuery() }.all().asFlow()
    }

    override fun findCustReqPossTstItems(searchParam: CustReqPossTstItemSearchParam, pageable: Pageable?): Flow<CustReqPossTstItemQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectFrom(SCS_CUST_REQ_POSS_TST_ITEM)
            .where(conditions)
            .orderBy(SCS_CUST_REQ_POSS_TST_ITEM.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustReqPossTstItemQuery() }.all().asFlow()
    }

    override suspend fun countCustReqPossTstItems(searchParam: CustReqPossTstItemSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_REQ_POSS_TST_ITEM)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustReqPossTstItemSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_REQ_POSS_TST_ITEM.CUST_MST_ID.eq(it)
        }
        return conds
    }
}
