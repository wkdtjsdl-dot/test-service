package com.idrsys.ailis.sales.adapter.repository.charge

import com.idrsys.ailis.sales.adapter.persistence.mapper.toChargeWithDetail
import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CHARGE
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
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
class ChargeCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
): ChargeCustomRepository {

    override fun findCharges(searchParam: ChargeSearchParam, pageable: Pageable): Flow<ChargeWithDetails> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_CUST_CHARGE.asterisk(),
            SCS_CUST_MST.CUST_NM,
            SCS_CUST_MST.BZOFFI_CD
        ).from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CHARGE.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(conditions)
            .orderBy(SCS_CUST_CHARGE.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toChargeWithDetail() }.all().asFlow()
    }

    override suspend fun countCharge(searchParam: ChargeSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CHARGE.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    private fun applyPaging(q: SelectLimitStep<out org.jooq.Record>, pageable: Pageable): Query {
        if(pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: ChargeSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.bzoffiCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.BZOFFI_CD.eq(it) }
        searchParam.lastApprStatCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq(it) }
        return conds
    }

}