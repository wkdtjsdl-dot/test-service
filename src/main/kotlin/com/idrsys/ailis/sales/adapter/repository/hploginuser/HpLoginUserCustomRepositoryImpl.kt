package com.idrsys.ailis.sales.adapter.repository.hploginuser

import com.idrsys.ailis.sales.adapter.persistence.mapper.toHpLoginUser
import com.idrsys.ailis.sales.adapter.persistence.mapper.toHpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.required.repository.hploginuser.HpLoginUserCustomRepository
import com.idrsys.ailis.sales.domain.model.HpLoginUser
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_HP_LOGIN_USER
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
class HpLoginUserCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : HpLoginUserCustomRepository {

    override fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserQuery> {
        val query = dslContext.selectFrom(SCS_HP_LOGIN_USER)
            .where(SCS_HP_LOGIN_USER.CUST_MST_ID.eq(searchParam.custMstId))
            .and(SCS_HP_LOGIN_USER.USE_YN.isTrue)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toHpLoginUserQuery() }.all().asFlow()
    }

    override fun findHpLoginUsers(searchParam: HpLoginUserSearchParam, pageable: Pageable?): Flow<HpLoginUserQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectFrom(SCS_HP_LOGIN_USER)
            .where(conditions)
            .orderBy(SCS_HP_LOGIN_USER.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toHpLoginUserQuery() }.all().asFlow()
    }

    override suspend fun countHpLoginUsers(searchParam: HpLoginUserSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_HP_LOGIN_USER)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findDomainById(id: String): HpLoginUser? {
        val query = dslContext.select(SCS_HP_LOGIN_USER.asterisk())
            .from(SCS_HP_LOGIN_USER)
            .where(SCS_HP_LOGIN_USER.HP_LOGIN_USER_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toHpLoginUser() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: HpLoginUserSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId.takeIf { it.isNotBlank() }?.let {
            conds += SCS_HP_LOGIN_USER.CUST_MST_ID.eq(it)
        }
        conds += SCS_HP_LOGIN_USER.USE_YN.isTrue
        return conds
    }
}
