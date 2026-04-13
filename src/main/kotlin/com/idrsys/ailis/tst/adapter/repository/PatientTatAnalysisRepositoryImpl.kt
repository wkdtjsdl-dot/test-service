package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisResponse
import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisSearchParam
import com.idrsys.ailis.tst.application.required.repository.PatientTatAnalysisRepository
import com.idrsys.ailis.tst.generated.jooq.tables.RbsPatient
import com.idrsys.ailis.tst.generated.jooq.tables.RbsTstItem
import com.idrsys.ailis.tst.generated.jooq.tables.TbsTstReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Repository
class PatientTatAnalysisRepositoryImpl(
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : PatientTatAnalysisRepository {

    override fun search(param: PatientTatAnalysisSearchParam): Flow<PatientTatAnalysisResponse> {
        val ti = RbsTstItem.RBS_TST_ITEM
        val p = RbsPatient.RBS_PATIENT
        val r = TbsTstReport.TBS_TST_REPORT

        var condition: Condition = ti.TST_REQ_DT.between(param.startDt, param.endDt)
        param.tstCd?.takeIf { it.isNotBlank() }?.let { condition = condition.and(ti.TST_CD.likeIgnoreCase("%$it%")) }

        val query = dslContext
            .select(
                p.SPCM_TAKE_DT.`as`("tst_req_dtime"),
                r.LIMS_RCV_DTIME.`as`("tst_end_dtime"),
                ti.TST_TAT_DT.`as`("expected_dt"),
                p.MEMO,
                ti.LIMS_TAT_DT.`as`("lims_expected_dt")
            )
            .from(ti)
            .leftJoin(p).on(p.TST_REQ_NO.eq(ti.TST_REQ_NO).and(p.TST_REQ_DT.eq(ti.TST_REQ_DT)))
            .leftJoin(r).on(
                r.TST_REQ_NO.eq(ti.TST_REQ_NO)
                    .and(r.TST_REQ_DT.eq(ti.TST_REQ_DT))
                    .and(r.TST_CD.eq(ti.TST_CD))
            )
            .where(condition)
            .orderBy(ti.TST_REQ_DT.desc(), ti.TST_REQ_NO.desc())

        var spec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            spec = if (value != null) spec.bind(index, value) else spec.bindNull(index, String::class.java)
        }

        return spec.fetch().all().map { row ->
            val tstReqDtime = row["tst_req_dtime"] as? LocalDateTime
            val tstEndDtime = row["tst_end_dtime"] as? LocalDateTime
            val expectedDt = row["expected_dt"] as? LocalDate

            // TAT 계산: 의뢰일시 → 종료일시 기준
            val tatHour = if (tstReqDtime != null && tstEndDtime != null)
                ChronoUnit.HOURS.between(tstReqDtime, tstEndDtime) else null
            val tatDay = tatHour?.let { it / 24 }

            // 초과 계산: 종료일 > 예정일
            val exceedDay = if (tstEndDtime != null && expectedDt != null) {
                val endDate = tstEndDtime.toLocalDate()
                if (endDate.isAfter(expectedDt)) ChronoUnit.DAYS.between(expectedDt, endDate) else 0L
            } else null
            val exceedHour = if (tstReqDtime != null && tstEndDtime != null && expectedDt != null) {
                val expectedDtime = expectedDt.atTime(23, 59, 59)
                if (tstEndDtime.isAfter(expectedDtime)) ChronoUnit.HOURS.between(expectedDtime, tstEndDtime) else 0L
            } else null

            PatientTatAnalysisResponse(
                tstEndDtime = tstEndDtime,
                expectedDt = expectedDt,
                tatDay = tatDay,
                tatHour = tatHour,
                exceedDay = exceedDay,
                exceedHour = exceedHour,
                memo = row["memo"] as? String,
                limsExpectedDt = row["lims_expected_dt"] as? LocalDate
            )
        }.asFlow()
    }
}
