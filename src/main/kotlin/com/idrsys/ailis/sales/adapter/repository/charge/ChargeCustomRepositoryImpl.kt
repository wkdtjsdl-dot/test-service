package com.idrsys.ailis.sales.adapter.repository.charge

import com.idrsys.ailis.sales.adapter.persistence.mapper.toCharge
import com.idrsys.ailis.sales.adapter.persistence.mapper.toChargeWithDetail
import com.idrsys.ailis.sales.adapter.persistence.mapper.toCustChargeInnerResponse
import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.domain.model.Charge
import com.idrsys.ailis.sales.generated.jooq.tables.ScsCustCharge.SCS_CUST_CHARGE
import com.idrsys.ailis.sales.generated.jooq.tables.ScsCustMst.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.tables.ScsGcgnSalsPicInfo.SCS_GCGN_SALS_PIC_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Query
import org.jooq.SelectLimitStep
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.jsonbArrayAgg
import org.jooq.impl.DSL.jsonbObject
import org.jooq.impl.DSL.key
import org.springframework.data.domain.Pageable
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class ChargeCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient,
): ChargeCustomRepository {

    override fun findCharges(searchParam: ChargeSearchParam, pageable: Pageable): Flow<ChargeWithDetails> {
        val conditions = buildConditions(searchParam)
        val salesPicsField = salesPicsFieldForCharge()
        val query = dslContext.select(
            SCS_CUST_CHARGE.asterisk(),
            SCS_CUST_MST.CUST_NM,
            SCS_CUST_MST.BZOFFI_CD,
            salesPicsField
        ).from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CHARGE.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(conditions)
            .orderBy(
                SCS_CUST_CHARGE.CUST_CD.asc(),
                SCS_CUST_CHARGE.TST_CD.asc(),
                SCS_CUST_CHARGE.APPLY_START_DT.desc()
            )

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

    override suspend fun findChargeWithDetailsById(custChargeId: String): ChargeWithDetails? {
        val salesPicsField = salesPicsFieldForCharge()
        val query = dslContext.select(
            SCS_CUST_CHARGE.asterisk(),
            SCS_CUST_MST.CUST_NM,
            SCS_CUST_MST.BZOFFI_CD,
            salesPicsField
        ).from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST).on(SCS_CUST_CHARGE.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
            .where(SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toChargeWithDetail() }
            .one()
            .awaitSingleOrNull()
    }

    private fun salesPicsFieldForCharge(): Field<*> {
        val subquery = dslContext.select(
            jsonbArrayAgg(
                jsonbObject(
                    key("emp_user_id").value(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID),
                    key("cust_mst_id").value(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID)
                )
            )
        ).from(SCS_GCGN_SALS_PIC_INFO)
            .where(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
        return field(subquery).`as`("sales_pics")
    }

    private fun applyPaging(q: SelectLimitStep<out org.jooq.Record>, pageable: Pageable): Query {
        if(pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }

    private fun buildConditions(searchParam: ChargeSearchParam): List<Condition> {
        val conds = mutableListOf<Condition>()
        searchParam.custMstId?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_MST_ID.likeIgnoreCase("%$it%") }
        searchParam.bzoffiCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.BZOFFI_CD.eq(it) }
        searchParam.tstCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_CHARGE.TST_CD.likeIgnoreCase("%$it%") }
        searchParam.lastApprStatCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq(it) }
        searchParam.refDt?.let { conds += SCS_CUST_CHARGE.APPLY_START_DT.le(it).and(SCS_CUST_CHARGE.APPLY_END_DT.ge(it)) }

        if (!searchParam.custCdNm.isNullOrBlank()) {
            conds += SCS_CUST_MST.CUST_CD.likeIgnoreCase("%${searchParam.custCdNm}%")
                .or(SCS_CUST_MST.CUST_NM.likeIgnoreCase("%${searchParam.custCdNm}%"))
        } else {
            searchParam.custCd?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_CD.likeIgnoreCase("%$it%") }
            searchParam.custNm?.takeIf { it.isNotBlank() }?.let { conds += SCS_CUST_MST.CUST_NM.likeIgnoreCase("%$it%") }
        }

        if (searchParam.includeHistory == false) {
            searchParam.searchDate?.let {
                conds += SCS_CUST_CHARGE.APPLY_START_DT.le(it).and(SCS_CUST_CHARGE.APPLY_END_DT.ge(it))
            }
        }

        if (searchParam.startDt != null && searchParam.endDt != null) {
            when (searchParam.dateSearchType) {
                "start" -> conds += SCS_CUST_CHARGE.APPLY_START_DT.between(searchParam.startDt, searchParam.endDt)
                "end" -> conds += SCS_CUST_CHARGE.APPLY_END_DT.between(searchParam.startDt, searchParam.endDt)
            }
        }

        val picConds = mutableListOf<Condition>()
        searchParam.empUserId?.takeIf { it.isNotBlank() }?.let { picConds.add(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.eq(it)) }
        searchParam.empUserIds.takeIf { it.isNotEmpty() }?.let { picConds.add(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.`in`(it)) }
        searchParam.empUserIdNm?.takeIf { it.isNotBlank() }?.let { picConds.add(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.likeIgnoreCase("%$it%")) }

        if (picConds.isNotEmpty()) {
            conds += DSL.exists(
                dslContext.selectOne()
                    .from(SCS_GCGN_SALS_PIC_INFO)
                    .where(SCS_GCGN_SALS_PIC_INFO.CUST_MST_ID.eq(SCS_CUST_MST.CUST_MST_ID))
                    .and(DSL.or(picConds))
            )
        }

        return conds
    }

    // UK 중복 검증
    override suspend fun existsByUniqueKey(
        custMstId: String,
        applyStartDt: LocalDate,
        tstCd: String,
        excludeId: String?
    ): Boolean {
        val conditions = mutableListOf<Condition>(
            SCS_CUST_CHARGE.CUST_MST_ID.eq(custMstId),
            SCS_CUST_CHARGE.APPLY_START_DT.eq(applyStartDt),
            SCS_CUST_CHARGE.TST_CD.eq(tstCd)
        )

        excludeId?.let { conditions += SCS_CUST_CHARGE.CUST_CHARGE_ID.ne(it) }

        val query = dslContext.selectCount()
            .from(SCS_CUST_CHARGE)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        val count = sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }
            .one()
            .awaitSingle()

        return count > 0
    }

    // 기간 겹침 검증
    override suspend fun findOverlappingPeriods(
        custMstId: String,
        tstCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        excludeId: String?
    ): List<Charge> {
        val conditions = mutableListOf<Condition>(
            SCS_CUST_CHARGE.CUST_MST_ID.eq(custMstId),
            SCS_CUST_CHARGE.TST_CD.eq(tstCd),
            SCS_CUST_CHARGE.APPLY_START_DT.le(endDt),
            SCS_CUST_CHARGE.APPLY_END_DT.ge(startDt)
        )

        excludeId?.let { conditions += SCS_CUST_CHARGE.CUST_CHARGE_ID.ne(it) }

        val query = dslContext.select(SCS_CUST_CHARGE.asterisk())
            .from(SCS_CUST_CHARGE)
            .where(conditions)

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toCharge() }
            .all()
            .asFlow()
            .toList()
    }

    override suspend fun findCustChargesByConditions(
        custCds: List<String>,
        tstCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate
    ): List<CustChargeInnerResponse> {
        if (custCds.isEmpty() || tstCds.isEmpty()) return emptyList()

        val query = dslContext.select(
            SCS_CUST_CHARGE.CUST_CD,
            SCS_CUST_CHARGE.TST_CD,
            SCS_CUST_CHARGE.APPLY_START_DT,
            SCS_CUST_CHARGE.APPLY_END_DT,
            SCS_CUST_CHARGE.STND_PRICE,
            SCS_CUST_CHARGE.CRCY_CD,
            SCS_CUST_CHARGE.SPECIAL_CHARGE,
            SCS_CUST_CHARGE.SUPVAL,
            SCS_CUST_CHARGE.ADDTAX
        )
            .from(SCS_CUST_CHARGE)
            .where(SCS_CUST_CHARGE.CUST_CD.`in`(custCds))
            .and(SCS_CUST_CHARGE.TST_CD.`in`(tstCds))
            .and(SCS_CUST_CHARGE.APPLY_START_DT.lessOrEqual(endDt))
            .and(SCS_CUST_CHARGE.APPLY_END_DT.greaterOrEqual(startDt))
            .orderBy(SCS_CUST_CHARGE.CUST_CD.asc(), SCS_CUST_CHARGE.TST_CD.asc())

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql
            .map { row, _ -> row.toCustChargeInnerResponse() }
            .all()
            .asFlow()
            .toList()
    }

    override suspend fun updateToInProgressWithCAS(
        custChargeId: String,
        apprInfoNo: Long,
        currApprSeq: Int,
        apprSubmsEmpNo: String,
        apprLvlCd: String,
        updater: String
    ): Int {
        val query = dslContext.update(SCS_CUST_CHARGE)
            .set(SCS_CUST_CHARGE.LAST_APPR_STAT_CD, "LAST_I")
            .set(SCS_CUST_CHARGE.APPR_INFO_NO, apprInfoNo)
            .set(SCS_CUST_CHARGE.CURR_APPR_SEQ, currApprSeq)
            .set(SCS_CUST_CHARGE.APPR_SUBMS_EMP_NO, apprSubmsEmpNo)
            .set(SCS_CUST_CHARGE.APPR_SUBMS_DTIME, LocalDateTime.now())
            .set(SCS_CUST_CHARGE.APPR_LVL_CD, apprLvlCd)
            .set(SCS_CUST_CHARGE.UPDATER, updater)
            .set(SCS_CUST_CHARGE.UPDATE_DTIME, LocalDateTime.now())
            .where(SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId).and(SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq("LAST_T")))

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }
        return sql.fetch().rowsUpdated().awaitSingle().toInt()
    }

    override suspend fun incrementApprSeqWithCAS(custChargeId: String, currentSeq: Int, newSeq: Int, updater: String): Int {
        val query = dslContext.update(SCS_CUST_CHARGE)
            .set(SCS_CUST_CHARGE.CURR_APPR_SEQ, newSeq)
            .set(SCS_CUST_CHARGE.UPDATER, updater)
            .set(SCS_CUST_CHARGE.UPDATE_DTIME, LocalDateTime.now())
            .where(
                SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId)
                    .and(SCS_CUST_CHARGE.CURR_APPR_SEQ.eq(currentSeq))
                    .and(SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq("LAST_I"))
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }
        return sql.fetch().rowsUpdated().awaitSingle().toInt()
    }

    override suspend fun completeApprovalWithCAS(custChargeId: String, currentSeq: Int, updater: String): Int {
        val query = dslContext.update(SCS_CUST_CHARGE)
            .set(SCS_CUST_CHARGE.LAST_APPR_STAT_CD, "LAST_C")
            .set(SCS_CUST_CHARGE.UPDATER, updater)
            .set(SCS_CUST_CHARGE.UPDATE_DTIME, LocalDateTime.now())
            .where(
                SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId)
                    .and(SCS_CUST_CHARGE.CURR_APPR_SEQ.eq(currentSeq))
                    .and(SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq("LAST_I"))
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }
        return sql.fetch().rowsUpdated().awaitSingle().toInt()
    }

    override suspend fun deleteWithCAS(custChargeId: String, currentSeq: Int): Int {
        val query = dslContext.deleteFrom(SCS_CUST_CHARGE)
            .where(
                SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId)
                    .and(SCS_CUST_CHARGE.CURR_APPR_SEQ.eq(currentSeq))
                    .and(SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq("LAST_I"))
            )

        var sql = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }
        return sql.fetch().rowsUpdated().awaitSingle().toInt()
    }
}

