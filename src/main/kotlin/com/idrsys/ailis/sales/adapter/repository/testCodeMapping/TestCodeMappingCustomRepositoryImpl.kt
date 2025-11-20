package com.idrsys.ailis.sales.adapter.repository.testCodeMapping

import com.idrsys.ailis.sales.adapter.persistence.mapper.toTestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.required.repository.testCodeMapping.TestCodeMappingCustomRepository
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_TST_CD_MPG
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
class TestCodeMappingCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
): TestCodeMappingCustomRepository {

    override fun findTestCodeMappings(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Flow<TestCodeMappingQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_CUST_TST_CD_MPG.asterisk(),
            SCS_CUST_MST.CUST_NM
        ).from(SCS_CUST_TST_CD_MPG)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_TST_CD_MPG.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .orderBy(SCS_CUST_TST_CD_MPG.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toTestCodeMappingQuery() }.all().asFlow()
    }


    override suspend fun countTestCodeMapping(searchParam: TestCodeMappingSearchParam): Long {
        val conditions = buildConditions(searchParam)

        val countQuery = dslContext.selectCount()
        val query = countQuery.from(SCS_CUST_TST_CD_MPG)

        val finalQuery = query.where(conditions)

        var sql = databaseClient.sql(finalQuery.sql)
        finalQuery.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)?.toLong() ?: 0L }.one().awaitSingleOrNull() ?: 0L
    }

    private fun buildConditions(searchParam: TestCodeMappingSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_TST_CD_MPG.CUST_CD.eq(it) }
        return conds
    }

    private fun applyPaging(q: SelectLimitStep<out org.jooq.Record>, pageable: Pageable): Query {
        if(pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }
}