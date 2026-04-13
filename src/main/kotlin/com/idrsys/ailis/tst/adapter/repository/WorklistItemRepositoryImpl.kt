package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.WorklistItemResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import com.idrsys.ailis.tst.application.required.repository.WorklistItemRepository
import com.idrsys.ailis.tst.generated.jooq.tables.BbsWrklistItm
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
import java.time.LocalDateTime

@Repository
class WorklistItemRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : WorklistItemRepository {

    override fun search(param: WorklistItemSearchParam): Flow<WorklistItemResponse> {
        val ti = RbsTstItem.RBS_TST_ITEM
        val p = RbsPatient.RBS_PATIENT
        val wi = BbsWrklistItm.BBS_WRKLIST_ITM
        val trn = DSL.table(DSL.name("tst_scm", "rbs_tst_trn")).`as`("trn")
        val trnReqDt = DSL.field(DSL.name("trn", "tst_req_dt"))
        val trnReqNo = DSL.field(DSL.name("trn", "tst_req_no"))
        val trnStatCd = DSL.field(DSL.name("trn", "tst_req_stat_cd"))
        val trnCreateDtime = DSL.field(DSL.name("trn", "create_dtime"))
        val cust = DSL.table(DSL.name("sales_scm", "scs_cust_mst")).`as`("cust")
        val custCd = DSL.field(DSL.name("cust", "cust_cd"))
        val custNm = DSL.field(DSL.name("cust", "cust_nm"))

        var condition: Condition = DSL.noCondition()
        param.startDt?.let { condition = condition.and(ti.TST_REQ_DT.greaterOrEqual(it)) }
        param.endDt?.let { condition = condition.and(ti.TST_REQ_DT.lessOrEqual(it)) }
        param.wrklistCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(wi.WRKLIST_CD.likeIgnoreCase("%$it%")) }
        param.tstCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.TST_CD.likeIgnoreCase("%$it%")) }
        param.spcmCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.SPCM_CD.likeIgnoreCase("%$it%")) }

        val query = dslContext
            .select(
                trnCreateDtime.`as`("lims_trans_dtime"),
                custNm.`as`("sub_cust_nm"),
                ti.TST_TAT_DT.`as`("expected_dt"),
                p.DIRECT_ACCT_BAR.`as`("other_inst_reg_no"),
                p.AGMT_REG_YN,
                p.REQ_REG_YN,
                ti.LIMS_TAT_DT.`as`("lims_expected_dt")
            )
            .from(ti)
            .innerJoin(wi).on(wi.TST_CD.eq(ti.TST_CD))
            .leftJoin(p).on(p.TST_REQ_NO.eq(ti.TST_REQ_NO).and(p.TST_REQ_DT.eq(ti.TST_REQ_DT)))
            .leftJoin(trn).on(
                trnReqDt.eq(ti.TST_REQ_DT)
                    .and(trnReqNo.eq(ti.TST_REQ_NO))
                    .and(trnStatCd.eq("RQST_I"))
            )
            .leftJoin(cust).on(custCd.eq(p.CUST_CD))
            .where(condition)
            .orderBy(ti.TST_REQ_DT.desc(), ti.TST_REQ_NO.desc())

        var spec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            spec = if (value != null) spec.bind(index, value) else spec.bindNull(index, String::class.java)
        }

        return spec.fetch().all().map { row ->
            WorklistItemResponse(
                limsTransDtime = row["lims_trans_dtime"] as? LocalDateTime,
                subCustNm = row["sub_cust_nm"] as? String,
                otherInstRegNo = row["other_inst_reg_no"] as? String,
                expectedDt = row["expected_dt"] as? LocalDate,
                agreeYn = row["agmt_reg_yn"] as? Boolean,
                reqDocYn = row["req_reg_yn"] as? Boolean,
                limsExpectedDt = row["lims_expected_dt"] as? LocalDate
            )
        }.asFlow()
    }
}
