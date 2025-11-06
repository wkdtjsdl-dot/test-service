package com.idrsys.ailis.sales.adapter.repository.salsAction

import com.idrsys.ailis.sales.application.dto.query.SalsActionQuery
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.application.required.repository.salsAction.SalsActionCustomRepository
import com.idrsys.ailis.sales.domain.model.SalsAction
import com.idrsys.ailis.sales.adapter.persistence.mapper.toSalsAction
import com.idrsys.ailis.sales.adapter.persistence.mapper.toSalsActionQuery
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
class SalsActionCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : SalsActionCustomRepository {

    override fun findSalsActions(searchParam: SalsActionSearchParam, pageable: Pageable?): Flow<SalsActionQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.asterisk()
        )
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION)
            .where(conditions)
            .orderBy(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toSalsActionQuery() }.all().asFlow()
    }

    override suspend fun countSalsActions(searchParam: SalsActionSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findSalsActionById(salsActionId: Long): SalsActionQuery? {
        val query = dslContext.select(
            com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.asterisk()
        )
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION)
            .where(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.SALS_ACTION_ID.eq(salsActionId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toSalsActionQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): SalsAction? {
        val query = dslContext.select(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.asterisk())
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION)
            .where(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.SALS_ACTION_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toSalsAction() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: SalsActionSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.CUST_MST_ID.eq(it)
        }
        searchParam.visitPrpsCd?.takeIf { it.isNotBlank() }?.let {
            conds += com.idrsys.ailis.sales.generated.jooq.Tables.SCS_SALS_ACTION.VISIT_PRPS_CD.eq(it)
        }
        return conds
    }
}
