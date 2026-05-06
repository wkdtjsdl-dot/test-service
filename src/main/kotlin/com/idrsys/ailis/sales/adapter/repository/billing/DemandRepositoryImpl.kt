package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.adapter.persistence.mapper.toDemand
import com.idrsys.ailis.sales.adapter.persistence.mapper.toDemandMonthlyInfo
import com.idrsys.ailis.sales.adapter.persistence.mapper.toDemandWithCustInfo
import com.idrsys.ailis.sales.application.dto.query.DemandMonthlyInfo
import com.idrsys.ailis.sales.application.dto.query.DemandWithCustInfo
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.domain.model.Demand
import com.idrsys.ailis.sales.generated.jooq.Tables.SBL_DEMAND
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
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

    override suspend fun existsByCustCdAndDemandStartDtAndDemandEndDtAndDemandType(
        custCd: String,
        demandStartDt: LocalDate,
        demandEndDt: LocalDate,
        demandType: String
    ): Boolean {
        return demandDataRepository.existsByCustCdAndDemandStartDtAndDemandEndDtAndDemandType(
            custCd,
            demandStartDt,
            demandEndDt,
            demandType
        )
    }

    // Custom query operations (implemented with jOOQ)
    override fun findDemands(searchParam: DemandSearchParam): Flow<Demand> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(SBL_DEMAND.asterisk())
            .from(SBL_DEMAND)
            .where(conditions)
            .orderBy(SBL_DEMAND.DEMAND_DT.desc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemand() }.all().asFlow()
    }

    override fun findDemandsWithCustInfo(searchParam: DemandSearchParam): Flow<DemandWithCustInfo> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SBL_DEMAND.asterisk(),
            SCS_CUST_MST.CUST_NM,
            SCS_CUST_MST.BZOFFI_CD,
            SCS_CUST_MST.SAP_CUST_CD
        )
            .from(SBL_DEMAND)
            .leftJoin(SCS_CUST_MST).on(SBL_DEMAND.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .orderBy(SBL_DEMAND.DEMAND_DT.desc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemandWithCustInfo() }.all().asFlow()
    }

    override suspend fun findDemandById(demandId: String): Demand? {
        val query = dslContext.select(SBL_DEMAND.asterisk())
            .from(SBL_DEMAND)
            .where(SBL_DEMAND.DEMAND_ID.eq(demandId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemand() }.one().awaitSingleOrNull()
    }

    override suspend fun findModifiableByMonth(startDt: LocalDate, endDt: LocalDate): List<DemandMonthlyInfo> {
        val query = dslContext.select(
            SBL_DEMAND.DEMAND_ID,
            SBL_DEMAND.CUST_CD,
            SBL_DEMAND.DEMAND_TYPE,
            SBL_DEMAND.CURR_CD,
            SBL_DEMAND.STND_PRICE,
            SBL_DEMAND.SUPVAL,
            SBL_DEMAND.ADDTAX,
            SBL_DEMAND.DEMAND_CHARGE,
            SBL_DEMAND.COLLEDGER_ID
        )
            .from(SBL_DEMAND)
            .where(
                SBL_DEMAND.DEMAND_START_DT.eq(startDt),
                SBL_DEMAND.DEMAND_END_DT.eq(endDt),
                SBL_DEMAND.SLSTMT_NO.isNull
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toDemandMonthlyInfo() }
            .all()
            .asFlow()
            .toList()
    }

    private fun buildConditions(searchParam: DemandSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        // Date range filter
        conds += SBL_DEMAND.DEMAND_DT.between(searchParam.startDt, searchParam.endDt)

        // Customer code filter
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let {
            conds += SBL_DEMAND.CUST_CD.eq(it)
        }

        searchParam.branchCd?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_MST.BZOFFI_CD.eq(it)
        }

        // Foreign account filter (via SCS_CUST_MST join) - null means all (no filter)
        searchParam.frgnAcctYn?.let {
            conds += SCS_CUST_MST.FRGN_ACCT_YN.eq(it)
        }

        // Note: SETTLED demands are all records in SBL_DEMAND table
        // (SLSTMT_NO is only set after SAP transmission, not at billing close time)

        return conds
    }
}
