package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.adapter.persistence.mapper.toDemand
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandType
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
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
import java.time.LocalDate

@Repository
class DemandRepositoryImpl(
    private val demandDataRepository: DemandDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : DemandRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(demand: Demand): Demand {
        return demandDataRepository.save(demand)
    }

    override suspend fun findById(id: String): Demand? {
        return demandDataRepository.findById(id)
    }

    override suspend fun delete(demand: Demand) {
        demandDataRepository.delete(demand)
    }

    override suspend fun existsByCustCdAndDemandStartDtAndDemandStndDt(
        custCd: String,
        demandStartDt: LocalDate,
        demandStndDt: LocalDate
    ): Boolean {
        return demandDataRepository.existsByCustCdAndDemandStartDtAndDemandStndDt(
            custCd,
            demandStartDt,
            demandStndDt
        )
    }

    // Custom query operations (implemented with jOOQ)
    override fun findDemands(searchParam: DemandSearchParam, pageable: Pageable): Flow<Demand> {
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

    override suspend fun countDemands(searchParam: DemandSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SBL_DEMAND)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingleOrNull() ?: 0L
    }

    override suspend fun findDemandById(demandId: String): Demand? {
        val query = dslContext.select(SBL_DEMAND.asterisk())
            .from(SBL_DEMAND)
            .where(SBL_DEMAND.DEMAND_ID.eq(demandId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemand() }.one().awaitSingleOrNull()
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

        // Demand type filter (settled only - unsettled is now from tst-service)
        if (searchParam.demandType == DemandType.SETTLED) {
            // Settled demands have sales statement number
            conds += SBL_DEMAND.SLSTMT_NO.isNotNull
        }

        return conds
    }
}