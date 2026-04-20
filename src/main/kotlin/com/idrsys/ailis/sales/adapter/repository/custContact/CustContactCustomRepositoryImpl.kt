package com.idrsys.ailis.sales.adapter.repository.custContact

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustContact
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustContactQuery
import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustContactPhnoResponse
import com.idrsys.ailis.sales.application.required.repository.custContact.CustContactCustomRepository
import com.idrsys.ailis.sales.domain.model.CustContact
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CONTACT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.jooq.impl.DSL
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
        val query = dslContext.selectFrom(SCS_CUST_CONTACT)
            .where(conditions)
            .orderBy(SCS_CUST_CONTACT.CREATE_DTIME.desc())
            .let { applyPaging(it, pageable ?: Pageable.unpaged()) }

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContactQuery() }.all().asFlow()
    }

    override suspend fun countCustContacts(searchParam: CustContactSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_CONTACT)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }.one().awaitSingle()
    }

    override suspend fun findCustContactById(custContactId: Long): CustContactQuery? {
        val query = dslContext.selectFrom(SCS_CUST_CONTACT)
            .where(SCS_CUST_CONTACT.CUST_CONTACT_ID.eq(custContactId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContactQuery() }.one().awaitSingleOrNull()
    }

    override suspend fun findDomainById(id: Long): CustContact? {
        val query = dslContext.select(SCS_CUST_CONTACT.asterisk())
            .from(SCS_CUST_CONTACT)
            .where(SCS_CUST_CONTACT.CUST_CONTACT_ID.eq(id))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCustContact() }.one().awaitSingleOrNull()
    }

    override suspend fun findPhnosByCustCds(custCdList: List<String>): List<CustContactPhnoResponse> {
        if (custCdList.isEmpty()) return emptyList()

        val rnField = DSL.rowNumber()
            .over(
                DSL.partitionBy(SCS_CUST_CONTACT.CUST_CD)
                    .orderBy(SCS_CUST_CONTACT.CREATE_DTIME.desc())
            )
            .`as`("rn")

        val sub = dslContext
            .select(SCS_CUST_CONTACT.CUST_CD, SCS_CUST_CONTACT.ACCT_CHARGE_NM, SCS_CUST_CONTACT.PHNO, SCS_CUST_CONTACT.TELNO, rnField)
            .from(SCS_CUST_CONTACT)
            .where(SCS_CUST_CONTACT.CUST_CD.`in`(custCdList))
            .and(SCS_CUST_CONTACT.USE_YN.isTrue)
            .asTable("t")

        val query = dslContext
            .select(
                DSL.field(DSL.name("t", "cust_cd"), String::class.java),
                DSL.field(DSL.name("t", "acct_charge_nm"), String::class.java),
                DSL.field(DSL.name("t", "phno"), String::class.java),
                DSL.field(DSL.name("t", "telno"), String::class.java)
            )
            .from(sub)
            .where(DSL.field(DSL.name("t", "rn"), Int::class.java).eq(1))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ ->
            CustContactPhnoResponse(
                custCd = row.get("cust_cd", String::class.java)!!,
                acctChargeNm = row.get("acct_charge_nm", String::class.java),
                phno = row.get("phno", String::class.java),
                telno = row.get("telno", String::class.java),
            )
        }.all().collectList().awaitSingle()
    }

    private fun applyPaging(q: SelectLimitStep<*>, pageable: Pageable?): Query {
        if (pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustContactSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_CONTACT.CUST_MST_ID.eq(it)
        }
        searchParam.acctChargeNm?.takeIf { it.isNotBlank() }?.let {
            conds += SCS_CUST_CONTACT.ACCT_CHARGE_NM.likeIgnoreCase("%${it}%")
        }
        return conds
    }
}
