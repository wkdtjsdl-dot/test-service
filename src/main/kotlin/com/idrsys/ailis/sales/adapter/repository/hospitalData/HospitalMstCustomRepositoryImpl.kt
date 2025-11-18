package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHospitalMst
import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMstCustomRepository
import com.idrsys.ailis.sales.domain.model.HospitalMst
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HOSP_MST
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
class HospitalMstCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : HospitalMstCustomRepository {

    override suspend fun countHospitalMstList(searchParam: HospitalDataSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_HOSP_MST)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override fun findHospitalMstList(searchParam: HospitalDataSearchParam, pageable: Pageable?): Flow<HospitalMst> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            SCS_HOSP_MST.asterisk()
        )
            .from(SCS_HOSP_MST)
            .where(conditions)
            .orderBy(SCS_HOSP_MST.CARE_INST_NM.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toHospitalMst() }.all().asFlow()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: HospitalDataSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.careInstNm?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_HOSP_MST.CARE_INST_NM.like("%$it%")
        }
        return conds
    }
}
