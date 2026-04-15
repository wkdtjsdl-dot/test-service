package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalResponse
import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalSearchParam
import com.idrsys.ailis.tst.application.required.repository.ChargeChangeApprovalRepository
import com.idrsys.ailis.tst.generated.jooq.tables.RbsPatient
import com.idrsys.ailis.tst.generated.jooq.tables.RbsTstItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ChargeChangeApprovalRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : ChargeChangeApprovalRepository {

    override fun search(param: ChargeChangeApprovalSearchParam): Flow<ChargeChangeApprovalResponse> {
        val ti = RbsTstItem.RBS_TST_ITEM
        val p = RbsPatient.RBS_PATIENT

        var condition: Condition = ti.CHARGE_CHANGE_CD.isNotNull
        param.startDt?.let { condition = condition.and(ti.TST_REQ_DT.greaterOrEqual(it)) }
        param.endDt?.let { condition = condition.and(ti.TST_REQ_DT.lessOrEqual(it)) }
        param.tstCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.TST_CD.likeIgnoreCase("%$it%")) }
        param.custCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(p.CUST_CD.likeIgnoreCase("%$it%")) }
        param.changeKindCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.CHARGE_CHANGE_CD.eq(it)) }

        val query = dslContext
            .select(
                ti.TST_REQ_DT,
                ti.TST_REQ_NO,
                ti.TST_CD,
                ti.CHARGE_CHANGE_CD.`as`("change_kind_cd"),
                ti.CLOSING_MEMO.`as`("memo")
            )
            .from(ti)
            .leftJoin(p).on(p.TST_REQ_NO.eq(ti.TST_REQ_NO).and(p.TST_REQ_DT.eq(ti.TST_REQ_DT)))
            .where(condition)
            .orderBy(ti.TST_REQ_DT.desc(), ti.TST_REQ_NO.desc())

        var spec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            spec = if (value != null) spec.bind(index, value) else spec.bindNull(index, String::class.java)
        }

        return spec.fetch().all().map { row ->
            ChargeChangeApprovalResponse(
                tstReqDt = row["tst_req_dt"] as LocalDate,
                tstReqNo = (row["tst_req_no"] as Number).toLong(),
                tstCd = row["tst_cd"] as String,
                changeKindNm = row["change_kind_cd"] as? String,
                memo = row["memo"] as? String
            )
        }.asFlow()
    }
}
