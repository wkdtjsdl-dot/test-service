
package com.idrsys.ailis.sales.adapter.repository.cust

import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_GCGN_SALS_PIC_INFO
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustWithSalsPicInfo
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.CustWithSalsPicInfo
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CustCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
) : CustCustomRepository {

    // TODO 작업중
    override fun findCustsWithSalsPicInfo(searchParam: CustSearchParam, pageable: Pageable?): Flow<CustWithSalsPicInfo> {
        val conditions = buildConditions(searchParam)

        val query = dslContext.select(
            SCS_CUST_MST.asterisk(),
            DSL.field("string_agg({0} || '=' || {1}, ',')", String::class.java,
                SCS_GCGN_SALS_PIC_INFO.SALS_TEAM_CD,
                SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID
            ).`as`("sals_pic_info")
        )
            .from(SCS_CUST_MST)
            .leftJoin(SCS_GCGN_SALS_PIC_INFO).on(SCS_CUST_MST.CUST_MST_ID.eq(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID))
            .where(conditions)
            .groupBy(*SCS_CUST_MST.fields())
            .orderBy(SCS_CUST_MST.CUST_CD.asc())
            .let {applyPaging(it, pageable ?: Pageable.unpaged())}

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustWithSalsPicInfo() }
            .all()
            .asFlow()
    }

    override suspend fun countCusts(searchParam: CustSearchParam): Long {
        val conditions = buildConditions(searchParam)
        val query = dslContext.selectCount()
            .from(SCS_CUST_MST)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }
            .one()
            .awaitSingle()
    }

    private fun applyPaging(q: SelectLimitStep<out Record>, pageable: Pageable?): Query {
        if(pageable == null || pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: CustSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()

        searchParam.custCdNm?.takeIf { it.isNotBlank() }?.let { keyword -> conds += SCS_CUST_MST.CUST_CD.likeIgnoreCase("%$keyword%").or(SCS_CUST_MST.CUST_NM.likeIgnoreCase("%$keyword%")) }
        searchParam.rprsCustCdNm?.takeIf { it.isNotBlank() }?.let { keyword -> conds += SCS_CUST_MST.RPRS_CUST_CD.likeIgnoreCase("%$keyword%").or(SCS_CUST_MST.RPRS_NM.likeIgnoreCase("%$keyword%")) }
        searchParam.custStatCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_STAT_CD.eq(it) }
        searchParam.regStartDt?.takeIf { it.isNotBlank() }?.let {
            val startCreateDtime = LocalDate.parse(it).atStartOfDay()
            conds += SCS_CUST_MST.CREATE_DTIME.ge(startCreateDtime)
        }
        searchParam.regEndDt?.takeIf { it.isNotBlank() }?.let {
            val endCreateDtime = LocalDate.parse(it).atTime(23, 59, 59, 999999999)
            conds += SCS_CUST_MST.CREATE_DTIME.lessOrEqual(endCreateDtime)
        }
        searchParam.custDivCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_DIV_CD.eq(it) }
        searchParam.asrtCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.ASRT_CD.likeIgnoreCase("%$it%") }
        searchParam.bizrno?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.BIZRNO.likeIgnoreCase("%$it%") }
        searchParam.careInstNo?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CARE_INST_NO.likeIgnoreCase("%$it%") }
        searchParam.frgnAcctYn?.let { conds += SCS_CUST_MST.FRGN_ACCT_YN.eq(it) }
        searchParam.studyProjCustYn?.let { conds += SCS_CUST_MST.STUDY_PROJ_CUST_YN.eq(it) }
        searchParam.sapCustCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.SAP_CUST_CD.likeIgnoreCase("%$it%") }
        searchParam.custTypeCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_TYPE_CD.eq(it) }

        return conds
    }

}