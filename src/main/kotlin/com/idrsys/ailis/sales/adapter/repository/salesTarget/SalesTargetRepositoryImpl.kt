package com.idrsys.ailis.sales.adapter.repository.salesTarget

import com.idrsys.ailis.sales.adapter.persistence.mapper.toSalesTargetDetailQuery
import com.idrsys.ailis.sales.adapter.persistence.mapper.toSalesTargetQuery
import com.idrsys.ailis.sales.application.dto.query.SalesTargetDetailQuery
import com.idrsys.ailis.sales.application.dto.query.SalesTargetQuery
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.application.required.repository.salesTarget.SalesTargetRepository
import com.idrsys.ailis.sales.domain.model.SalesTarget
import com.idrsys.ailis.sales.generated.jooq.tables.SblSalesTarget.SBL_SALES_TARGET
import com.idrsys.ailis.sales.generated.jooq.tables.ScsCustMst.SCS_CUST_MST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class SalesTargetRepositoryImpl(
    private val salesTargetDataRepository: SalesTargetDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : SalesTargetRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(salesTarget: SalesTarget): SalesTarget {
        return salesTargetDataRepository.save(salesTarget)
    }

    override suspend fun findById(id: String): SalesTarget? {
        return salesTargetDataRepository.findById(id)
    }

    override suspend fun delete(salesTarget: SalesTarget) {
        salesTargetDataRepository.delete(salesTarget)
    }

    override suspend fun findByCustCdAndSalesYearAndSalesMonthAndSalsTeamCd(
        custCd: String,
        salesYear: String,
        salesMonth: String,
        salsTeamCd: String
    ): SalesTarget? {
        return salesTargetDataRepository.findByCustCdAndSalesYearAndSalesMonthAndSalsTeamCd(
            custCd, salesYear, salesMonth, salsTeamCd
        )
    }

    // Custom query operations (implemented with jOOQ)
    override fun findSalesTargets(searchParam: SalesTargetSearchParam): Flow<SalesTargetQuery> {
        val conditions = buildConditions(searchParam)

        val query = dslContext.select(
            SBL_SALES_TARGET.SALES_TARGET_ID,
            SBL_SALES_TARGET.SALES_YEAR,
            SBL_SALES_TARGET.CUST_CD,
            SCS_CUST_MST.CUST_NM,
            SBL_SALES_TARGET.SALS_TEAM_CD,
            DSL.inline(null as String?).`as`("sals_team_nm"),
            DSL.sum(SBL_SALES_TARGET.MONTH_SALES_TARGET_AMT).`as`("total_target"),
            DSL.sum(SBL_SALES_TARGET.PAST_YEAR_MONTH_SALES_AMT).`as`("prev_year_sales")
        )
            .from(SBL_SALES_TARGET)
            .join(SCS_CUST_MST).on(SBL_SALES_TARGET.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .groupBy(
                SBL_SALES_TARGET.SALES_TARGET_ID,
                SBL_SALES_TARGET.SALES_YEAR,
                SBL_SALES_TARGET.CUST_CD,
                SCS_CUST_MST.CUST_NM,
                SBL_SALES_TARGET.SALS_TEAM_CD
            )
            .orderBy(
                SBL_SALES_TARGET.CUST_CD.asc(),
                SBL_SALES_TARGET.SALS_TEAM_CD.asc()
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toSalesTargetQuery() }.all().asFlow()
    }

    override fun findSalesTargetDetails(searchParam: SalesTargetDetailSearchParam): Flow<SalesTargetDetailQuery> {
        val conditions = buildDetailConditions(searchParam)

        val query = dslContext.select(
            SBL_SALES_TARGET.SALES_TARGET_ID,
            SBL_SALES_TARGET.SALES_YEAR,
            SBL_SALES_TARGET.SALES_MONTH,
            SBL_SALES_TARGET.CUST_CD,
            SCS_CUST_MST.CUST_NM,
            SBL_SALES_TARGET.SALS_TEAM_CD,
            DSL.inline(null as String?).`as`("sals_team_nm"),
            SBL_SALES_TARGET.MONTH_SALES_TARGET_AMT.`as`("monthly_target"),
            SBL_SALES_TARGET.PAST_YEAR_MONTH_SALES_AMT.`as`("prev_year_sales")
        )
            .from(SBL_SALES_TARGET)
            .join(SCS_CUST_MST).on(SBL_SALES_TARGET.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .orderBy(
                SBL_SALES_TARGET.SALES_MONTH.asc(),
                SBL_SALES_TARGET.SALS_TEAM_CD.asc()
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toSalesTargetDetailQuery() }.all().asFlow()
    }

    private fun buildConditions(searchParam: SalesTargetSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        conds += SBL_SALES_TARGET.SALES_YEAR.eq(searchParam.year.toString())

        searchParam.directAcctCd?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_MST.DIRECT_ACCT_CD.eq(it)
        }

        return conds
    }

    private fun buildDetailConditions(searchParam: SalesTargetDetailSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        conds += SBL_SALES_TARGET.SALES_YEAR.eq(searchParam.year.toString())
        conds += SBL_SALES_TARGET.CUST_CD.eq(searchParam.custCd)

        return conds
    }
}
