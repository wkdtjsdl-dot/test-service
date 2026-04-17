package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.WorklistItemStatResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import com.idrsys.ailis.tst.application.required.repository.WorklistItemRepository
import com.idrsys.ailis.tst.generated.jooq.tables.BbsWrklistItm
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem
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

    override fun search(param: WorklistItemSearchParam): Flow<WorklistItemStatResponse> {
        val spec = buildSpec(param)
        return spec.fetch().all().map { row -> row.toWorklistItemStatResponse() }.asFlow()
    }

    override fun searchForExcel(param: WorklistItemSearchParam): Flow<WorklistItemStatResponse> = search(param)

    private fun buildSpec(param: WorklistItemSearchParam): DatabaseClient.GenericExecuteSpec {
        val ti = RbsTstItem.RBS_TST_ITEM
        val p = RbsPatient.RBS_PATIENT
        val wi = BbsWrklistItm.BBS_WRKLIST_ITM
        val bi = BtsItem.BTS_ITEM
        // 거래처명: direct_acct_cd 기준
        val custMain = DSL.table(DSL.name("sales_scm", "scs_cust_mst")).`as`("cust_main")
        val custMainCd = DSL.field(DSL.name("cust_main", "cust_cd"))
        val custMainNm = DSL.field(DSL.name("cust_main", "cust_nm"))
        // 부속거래처명: cust_cd 기준
        val custSub = DSL.table(DSL.name("sales_scm", "scs_cust_mst")).`as`("cust_sub")
        val custSubCd = DSL.field(DSL.name("cust_sub", "cust_cd"))
        val custSubNm = DSL.field(DSL.name("cust_sub", "cust_nm"))
        // 타기관등록번호: rup_api_req_stg.cust_tst_req_no
        val rupStg = DSL.table(DSL.name("tst_scm", "rup_api_req_stg")).`as`("rup_stg")
        val rupCustTstReqNo = DSL.field(DSL.name("rup_stg", "cust_tst_req_no"))
        // 관리그룹: scs_cust_mst.bzoffi_cd → cco_dept.dept_nm
        val ccoDept = DSL.table(DSL.name("base_scm", "cco_dept")).`as`("cco_dept")
        val ccoDeptCd = DSL.field(DSL.name("cco_dept", "dept_cd"))
        val ccoDeptNm = DSL.field(DSL.name("cco_dept", "dept_nm"))
        val custMainBzoffiCd = DSL.field(DSL.name("cust_main", "bzoffi_cd"))

        var wiCondition: Condition = wi.TST_CD.eq(ti.TST_CD)
        param.wrklistCd?.takeIf { it.isNotBlank() }?.let {
            wiCondition = wiCondition.and(wi.WRKLIST_CD.likeIgnoreCase("%$it%"))
        }

        var condition: Condition = DSL.exists(DSL.selectOne().from(wi).where(wiCondition))
            .and(ti.TST_REQ_STAT_CD.eq("RQST_F"))
        param.startDt?.let { condition = condition.and(ti.TST_REQ_DT.greaterOrEqual(it)) }
        param.endDt?.let { condition = condition.and(ti.TST_REQ_DT.lessOrEqual(it)) }
        param.tstCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.TST_CD.likeIgnoreCase("%$it%")) }
        param.spcmCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.SPCM_CD.likeIgnoreCase("%$it%")) }

        val query = dslContext
            .select(
                ti.TST_REQ_DT,
                ti.TST_REQ_NO,
                p.DIRECT_ACCT_CD.`as`("cust_cd"),
                custMainNm.`as`("cust_nm"),
                p.PAT_NM,
                p.PAT_AGE,
                p.PAT_GENDER_CD,
                p.PAT_BDAY,
                p.HOSP_CHART_NO,
                p.TST_REQ_STAT_CD.`as`("stat_nm"),
                ti.TST_CD,
                bi.TST_NM,
                p.MEDI_SBJT_NM.`as`("dept_nm"),
                ccoDeptNm.`as`("mng_group"),
                p.MEMO,
                p.REMARK,
                ti.CREATE_DTIME.`as`("order_reg_dtime"),
                ti.LIMS_APPLY_DTIME.`as`("lims_apply_dtime"),
                custSubNm.`as`("sub_cust_nm"),
                rupCustTstReqNo.`as`("other_inst_reg_no"),
                ti.TST_TAT_DT.`as`("expected_dt"),
                p.AGMT_REG_YN,
                p.REQ_REG_YN,
                ti.LIMS_TAT_DT.`as`("lims_expected_dt")
            )
            .from(ti)
            .leftJoin(p).on(p.TST_REQ_NO.eq(ti.TST_REQ_NO).and(p.TST_REQ_DT.eq(ti.TST_REQ_DT)))
            .leftJoin(bi).on(bi.TST_CD.eq(ti.TST_CD))
            .leftJoin(custMain).on(custMainCd.eq(p.DIRECT_ACCT_CD))
            .leftJoin(custSub).on(custSubCd.eq(p.CUST_CD))
            .leftJoin(ccoDept).on(ccoDeptCd.eq(custMainBzoffiCd))
            .leftJoin(rupStg).on(
                DSL.field(DSL.name("rup_stg", "tst_req_dt")).eq(ti.TST_REQ_DT)
                    .and(DSL.field(DSL.name("rup_stg", "tst_req_no")).eq(ti.TST_REQ_NO))
                    .and(DSL.field(DSL.name("rup_stg", "tst_cd")).eq(ti.TST_CD))
            )
            .where(condition)
            .orderBy(ti.TST_REQ_DT.desc(), ti.TST_REQ_NO.desc())

        var spec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            spec = if (value != null) spec.bind(index, value) else spec.bindNull(index, String::class.java)
        }
        return spec
    }

    private fun Map<String, Any>.toWorklistItemStatResponse() = WorklistItemStatResponse(
        reqDt = this["tst_req_dt"] as? LocalDate,
        tstReqNo = (this["tst_req_no"] as? Number)?.toLong(),
        custCd = this["cust_cd"] as? String,
        custNm = this["cust_nm"] as? String,
        patNm = this["pat_nm"] as? String,
        patAge = (this["pat_age"] as? Number)?.toInt(),
        patGender = this["pat_gender_cd"] as? String,
        patBday = this["pat_bday"] as? LocalDate,
        chartNo = this["hosp_chart_no"] as? String,
        statNm = this["stat_nm"] as? String,
        tstCd = this["tst_cd"] as? String,
        tstNm = this["tst_nm"] as? String,
        deptNm = this["dept_nm"] as? String,
        mngGroup = this["mng_group"] as? String,
        memo = this["memo"] as? String,
        remark = this["remark"] as? String,
        orderRegDtime = this["order_reg_dtime"] as? LocalDateTime,
        limsApplyDtime = this["lims_apply_dtime"] as? LocalDateTime,
        subCustNm = this["sub_cust_nm"] as? String,
        otherInstRegNo = this["other_inst_reg_no"] as? String,
        expectedDt = this["expected_dt"] as? LocalDate,
        agreeYn = this["agmt_reg_yn"] as? Boolean,
        reqDocYn = this["req_reg_yn"] as? Boolean,
        limsExpectedDt = this["lims_expected_dt"] as? LocalDate
    )
}
