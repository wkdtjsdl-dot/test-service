package com.idrsys.ailis.sales.adapter.repository.apprinfo

import com.idrsys.ailis.sales.adapter.persistence.mapper.ApprInfoRowMapper
import com.idrsys.ailis.sales.adapter.persistence.mapper.toChargeApproveQuery
import com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoCustomRepository
import com.idrsys.ailis.sales.domain.model.ApprInfo
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_APPR_INFO
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_CHARGE
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_CUST_MST
import com.idrsys.ailis.sales.generated.jooq.Tables.SCS_GCGN_SALS_PIC_INFO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

/**
 * 결재정보 조회 Repository 구현체 (jOOQ)
 */
@Repository
class ApprInfoCustomRepositoryImpl(
    private val dslContext: DSLContext,
    private val dbClient: DatabaseClient,
) : ApprInfoCustomRepository {

    override fun findByApprInfoNo(apprInfoNo: Long): Flow<ApprInfo> {
        val sql = dslContext
            .select(SCS_APPR_INFO.asterisk())
            .from(SCS_APPR_INFO)
            .where(SCS_APPR_INFO.APPR_INFO_NO.eq(apprInfoNo))
            .orderBy(SCS_APPR_INFO.APPR_SEQ)
            .sql

        return dbClient.sql(sql)
            .map { row, _ -> ApprInfoRowMapper.mapRow(row) }
            .all()
            .asFlow()
    }

    override suspend fun findPendingApprovalByChargeId(custChargeId: String): ApprInfo? {
        val sql = dslContext
            .select(SCS_APPR_INFO.asterisk())
            .from(SCS_APPR_INFO)
            .join(SCS_CUST_CHARGE)
            .on(SCS_APPR_INFO.APPR_INFO_NO.eq(SCS_CUST_CHARGE.APPR_INFO_NO.cast(Long::class.java)))
            .where(
                SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId)
                    .and(SCS_APPR_INFO.APPR_SEQ.eq(SCS_CUST_CHARGE.CURR_APPR_SEQ))
                    .and(SCS_APPR_INFO.APPR_STAT_CD.eq("APST_W"))  // 대기중
            )
            .sql

        return dbClient.sql(sql)
            .map { row, _ -> ApprInfoRowMapper.mapRow(row) }
            .one()
            .awaitSingleOrNull()
    }

    override fun findApprovalCharges(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        userRole: String,
        empNo: String?,
        pageable: Pageable
    ): Flow<ChargeApproveQuery> {
        val conditions = buildApprovalConditions(searchParam, userId, userRole, empNo)

        val query = dslContext.select(
            SCS_CUST_CHARGE.CUST_CHARGE_ID,
            SCS_CUST_CHARGE.CUST_CD,
            SCS_CUST_MST.CUST_NM,  // custNm from join
            SCS_CUST_CHARGE.TST_CD,
            SCS_CUST_CHARGE.APPLY_START_DT,
            SCS_CUST_CHARGE.APPLY_END_DT,
            SCS_CUST_CHARGE.SPECIAL_CHARGE,
            SCS_CUST_CHARGE.STND_PRICE,
            SCS_CUST_CHARGE.SUPVAL,
            SCS_CUST_CHARGE.ADDTAX,
            SCS_CUST_CHARGE.REMARK,
            SCS_CUST_CHARGE.APPR_INFO_NO,
            SCS_CUST_CHARGE.CURR_APPR_SEQ,
            SCS_CUST_CHARGE.APPR_SUBMS_EMP_NO,
            SCS_CUST_CHARGE.APPR_SUBMS_DTIME,
            SCS_CUST_CHARGE.LAST_APPR_STAT_CD,
            SCS_CUST_CHARGE.APPR_LVL_CD
        )
            .from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST)
            .on(SCS_CUST_CHARGE.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)
            .orderBy(SCS_CUST_CHARGE.APPR_SUBMS_DTIME.desc())
            .let { applyPaging(it, pageable) }

        var sql = dbClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.toChargeApproveQuery() }.all().asFlow()
    }

    override suspend fun countApprovalCharges(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        userRole: String,
        empNo: String?
    ): Long {
        val conditions = buildApprovalConditions(searchParam, userId, userRole, empNo)

        val query = dslContext.selectCount()
            .from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST)
            .on(SCS_CUST_CHARGE.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(conditions)

        var sql = dbClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        return sql.map { row, _ -> row.get(0, java.lang.Long::class.java)!!.toLong() }
            .one()
            .awaitSingle()
    }

    override suspend fun findApprovalChargeWithLines(custChargeId: String): Pair<ChargeApproveQuery, List<ApprInfo>>? {
        // 1. Get charge details
        val chargeQuery = dslContext.select(
            SCS_CUST_CHARGE.CUST_CHARGE_ID,
            SCS_CUST_CHARGE.CUST_CD,
            SCS_CUST_MST.CUST_NM,
            SCS_CUST_CHARGE.TST_CD,
            SCS_CUST_CHARGE.APPLY_START_DT,
            SCS_CUST_CHARGE.APPLY_END_DT,
            SCS_CUST_CHARGE.SPECIAL_CHARGE,
            SCS_CUST_CHARGE.STND_PRICE,
            SCS_CUST_CHARGE.SUPVAL,
            SCS_CUST_CHARGE.ADDTAX,
            SCS_CUST_CHARGE.REMARK,
            SCS_CUST_CHARGE.APPR_INFO_NO,
            SCS_CUST_CHARGE.CURR_APPR_SEQ,
            SCS_CUST_CHARGE.APPR_SUBMS_EMP_NO,
            SCS_CUST_CHARGE.APPR_SUBMS_DTIME,
            SCS_CUST_CHARGE.LAST_APPR_STAT_CD,
            SCS_CUST_CHARGE.APPR_LVL_CD
        )
            .from(SCS_CUST_CHARGE)
            .leftJoin(SCS_CUST_MST)
            .on(SCS_CUST_CHARGE.CUST_CD.eq(SCS_CUST_MST.CUST_CD))
            .where(SCS_CUST_CHARGE.CUST_CHARGE_ID.eq(custChargeId))

        var sql = dbClient.sql(chargeQuery.sql)
        chargeQuery.bindValues.forEachIndexed { i, v -> sql = sql.bind(i, v) }

        val charge = sql.map { row, _ -> row.toChargeApproveQuery() }
            .one()
            .awaitSingleOrNull() ?: return null

        // 2. Get approval lines if apprInfoNo exists
        val approvalLines = if (charge.apprInfoNo != null) {
            findByApprInfoNo(charge.apprInfoNo).toList()
        } else {
            emptyList()
        }

        return Pair(charge, approvalLines)
    }

    private fun buildApprovalConditions(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        userRole: String,
        empNo: String?
    ): List<Condition> {
        val conditions = mutableListOf<Condition>()

        // Role-based filtering
        when (userRole) {
            "JP_TM", "JP_PM" -> {
                // Team members: filter by customer ownership
                conditions += DSL.exists(
                    dslContext.selectOne()
                        .from(SCS_GCGN_SALS_PIC_INFO)
                        .where(
                            SCS_GCGN_SALS_PIC_INFO.CUST_CD.eq(SCS_CUST_CHARGE.CUST_CD)
                                .and(SCS_GCGN_SALS_PIC_INFO.EMP_USER_ID.eq(userId))
                        )
                )
            }
            "JP_TL", "JP_DH", "JP_P" -> {
                // Approvers: filter by approval authority
                if (empNo != null) {
                    conditions += DSL.exists(
                        dslContext.selectOne()
                            .from(SCS_APPR_INFO)
                            .where(
                                SCS_APPR_INFO.APPR_INFO_NO.eq(
                                    SCS_CUST_CHARGE.APPR_INFO_NO.cast(Long::class.java)
                                )
                                    .and(SCS_APPR_INFO.APPR_SEQ.eq(SCS_CUST_CHARGE.CURR_APPR_SEQ))
                                    .and(SCS_APPR_INFO.APPR_PERSON_EMP_NO.eq(empNo))
                                    .and(SCS_APPR_INFO.APPR_STAT_CD.eq("APST_W"))  // Waiting only
                            )
                    )
                }
            }
        }

        // Search parameter conditions
        searchParam.custCd?.takeIf { it.isNotBlank() }?.let {
            conditions += SCS_CUST_CHARGE.CUST_CD.likeIgnoreCase("%$it%")
        }
        searchParam.tstCd?.takeIf { it.isNotBlank() }?.let {
            conditions += SCS_CUST_CHARGE.TST_CD.likeIgnoreCase("%$it%")
        }
        searchParam.applyStartDt?.let {
            conditions += SCS_CUST_CHARGE.APPLY_START_DT.ge(it)
        }
        searchParam.applyEndDt?.let {
            conditions += SCS_CUST_CHARGE.APPLY_END_DT.le(it)
        }
        searchParam.lastApprStatCd?.takeIf { it.isNotBlank() }?.let {
            conditions += SCS_CUST_CHARGE.LAST_APPR_STAT_CD.eq(it)
        }
        searchParam.apprLvlCd?.takeIf { it.isNotBlank() }?.let {
            conditions += SCS_CUST_CHARGE.APPR_LVL_CD.eq(it)
        }

        return conditions
    }

    private fun applyPaging(q: org.jooq.SelectLimitStep<out org.jooq.Record>, pageable: Pageable): org.jooq.Query {
        if (pageable.isUnpaged) return q
        else return q.limit(pageable.pageSize).offset(pageable.offset)
    }
}
