package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.adapter.persistence.mapper.toDemand
import com.idrsys.ailis.sales.adapter.persistence.mapper.toUnsettledDemandSummary
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandType
import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import com.idrsys.ailis.sales.domain.model.Demand
import com.idrsys.ailis.sales.generated.jooq.Tables.SBL_DEMAND
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class DemandCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) {

    fun findDemands(searchParam: DemandSearchParam, pageable: Pageable): Flow<Demand> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(SBL_DEMAND.asterisk())
            .from(SBL_DEMAND)
            .where(conditions)
            .orderBy(SBL_DEMAND.DEMAND_DT.desc())
            .let { applyPaging(it, pageable) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemand() }.all().asFlow()
    }

    suspend fun countDemands(searchParam: DemandSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SBL_DEMAND)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingleOrNull() ?: 0L
    }

    suspend fun findDemandById(demandId: String): Demand? {
        val query = dslContext.select(SBL_DEMAND.asterisk())
            .from(SBL_DEMAND)
            .where(SBL_DEMAND.DEMAND_ID.eq(demandId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemand() }.one().awaitSingleOrNull()
    }

    fun findUnsettledDemandSummary(searchParam: DemandSearchParam, pageable: Pageable): Flow<UnsettledDemandSummary> {
        // This is a placeholder implementation
        // In a real scenario, this would join with collection request tables and group by customer
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SBL_DEMAND.CUST_CD.`as`("cust_cd"),
            org.jooq.impl.DSL.inline(null as String?).`as`("cust_nm"),
            org.jooq.impl.DSL.inline(null as String?).`as`("branch_nm"),
            SBL_DEMAND.DEMAND_STND_DT.`as`("demand_stnd_dt"),
            org.jooq.impl.DSL.sum(SBL_DEMAND.STND_PRICE).`as`("stnd_price"),
            org.jooq.impl.DSL.sum(SBL_DEMAND.SUPVAL).`as`("supval"),
            org.jooq.impl.DSL.sum(SBL_DEMAND.ADDTAX).`as`("addtax"),
            org.jooq.impl.DSL.sum(SBL_DEMAND.DEMAND_CHARGE).`as`("demand_charge"),
            org.jooq.impl.DSL.count().`as`("request_count")
        )
            .from(SBL_DEMAND)
            .where(conditions)
            .groupBy(SBL_DEMAND.CUST_CD, SBL_DEMAND.DEMAND_STND_DT)
            .orderBy(SBL_DEMAND.DEMAND_STND_DT.desc())
            .let { applyPaging(it, pageable) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toUnsettledDemandSummary() }.all().asFlow()
    }

    suspend fun countUnsettledDemandSummary(searchParam: DemandSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(org.jooq.impl.DSL.countDistinct(SBL_DEMAND.CUST_CD))
            .from(SBL_DEMAND)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingleOrNull() ?: 0L
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable): Query {
        if (pageable.isUnpaged) return q
        return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: DemandSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        // Date range filter
        conds += SBL_DEMAND.DEMAND_STND_DT.between(searchParam.startDt, searchParam.endDt)

        // Customer code filter
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let {
            conds += SBL_DEMAND.CUST_CD.eq(it)
        }

        // Demand type filter (settled or unsettled)
        when (searchParam.demandType) {
            DemandType.SETTLED -> {
                // Settled demands have sales statement number
                conds += SBL_DEMAND.SLSTMT_NO.isNotNull
            }
            DemandType.UNSETTLED -> {
                // Unsettled demands don't have sales statement number
                conds += SBL_DEMAND.SLSTMT_NO.isNull
            }
        }

        return conds
    }
}