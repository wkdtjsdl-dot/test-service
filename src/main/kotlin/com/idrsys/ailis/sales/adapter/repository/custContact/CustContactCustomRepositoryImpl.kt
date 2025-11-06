package com.idrsys.ailis.sales.adapter.repository.custContact

import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.application.required.repository.custContact.CustContactCustomRepository
import com.idrsys.ailis.sales.domain.model.CustContact
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustContact
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustContactQuery
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
class CustContactCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustContactCustomRepository {

    override fun findCustContacts(searchParam: CustContactSearchParam, pageable: Pageable?): Flow<CustContactQuery> {
        val conditions = buildConditions(searchParam)
        val query = dslContext.select(
            com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.asterisk()
        )
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT)
            .where(conditions)
            .orderBy(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContactQuery() }.all().asFlow()
    }

    override suspend fun countCustContacts(searchParam: CustContactSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findCustContactById(custContactId: Long): CustContactQuery? {
        val query = dslContext.select(
            com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.asterisk()
        )
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT)
            .where(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.CUST_CONTACT_ID.eq(custContactId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContactQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): CustContact? {
        val query = dslContext.select(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.asterisk())
            .from(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT)
            .where(com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.CUST_CONTACT_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContact() }.one().awaitSingleOrNull()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustContactSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.CUST_MST_ID.eq(it)
        }
        searchParam.acctChargeNm?.takeIf { it.isNotBlank() }?.let {
            conds += com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT.ACCT_CHARGE_NM.likeIgnoreCase("%${it}%")
        }
        return conds
    }
}
