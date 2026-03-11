package com.idrsys.ailis.sales.adapter.repository.contract

import com.idrsys.ailis.sales.adapter.persistence.mapper.toContract
import com.idrsys.ailis.sales.adapter.persistence.mapper.toContractWithDetails
import com.idrsys.ailis.sales.application.dto.query.ContractWithDetails
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.application.required.repository.contract.ContractCustomRepository
import com.idrsys.ailis.sales.domain.model.Contract
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CNTR
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.shared.mapper.ContractMapper
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
class ContractCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
    private val contractMapper: ContractMapper,
) : ContractCustomRepository {

    override fun findContracts(searchParam: ContractSearchParam, pageable: Pageable?): Flow<ContractWithDetails> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_CUST_CNTR.asterisk(),
            SCS_CUST_MST.CUST_NM
        )
            .from(SCS_CUST_CNTR)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CNTR.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(conditions)
            .orderBy(SCS_CUST_CNTR.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toContractWithDetails() }.all().asFlow()
    }

    override suspend fun countContracts(searchParam: ContractSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_CNTR)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findContractById(custCntrId: Long): ContractWithDetails? {
        val query = dslContext.select(
            SCS_CUST_CNTR.asterisk(),
            SCS_CUST_MST.CUST_NM
        )
            .from(SCS_CUST_CNTR)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CNTR.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(SCS_CUST_CNTR.CUST_CNTR_ID.eq(custCntrId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toContractWithDetails() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): Contract? {
        val query = dslContext.select(SCS_CUST_CNTR.asterisk())
            .from(SCS_CUST_CNTR)
            .where(SCS_CUST_CNTR.CUST_CNTR_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toContract() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: ContractSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_CNTR.CUST_MST_ID.eq(it)
        }
        return conds
    }

}
