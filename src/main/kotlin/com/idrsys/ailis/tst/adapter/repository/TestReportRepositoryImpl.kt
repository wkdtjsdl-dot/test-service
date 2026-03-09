package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.application.required.repository.TestReportRepository
import com.idrsys.ailis.tst.domain.model.TestReport
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem.BTS_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.RbsPatient.RBS_PATIENT
import com.idrsys.ailis.tst.generated.jooq.tables.RbsTstItem.RBS_TST_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.TbsTstReport.TBS_TST_REPORT
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem.BBS_DEPT_TST_ITEM
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.DSLContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * R2DBC Repository Interface
 */
@Repository
interface TestReportDataRepository : CoroutineCrudRepository<TestReport, String>

/**
 * 검사결과 보고서 Repository 구현체
 */
@Repository
class TestReportRepositoryImpl(
    private val testReportDataRepository: TestReportDataRepository,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : TestReportRepository {

    override suspend fun save(testReport: TestReport): TestReport {
        return testReportDataRepository.save(testReport)
    }

    override suspend fun findById(id: String): TestReport? {
        return testReportDataRepository.findById(id)
    }

    override suspend fun findByReqAndTestCode(
        tstReqDt: LocalDate,
        tstReqNo: Long,
        tstCd: String
    ): TestReport? {
        val criteria = Criteria.where("tst_req_dt").`is`(tstReqDt)
            .and("tst_req_no").`is`(tstReqNo)
            .and("tst_cd").`is`(tstCd)

        return r2dbcEntityTemplate
            .selectOne(Query.query(criteria), TestReport::class.java)
            .awaitSingleOrNull()
    }

    override suspend fun searchTestResults(params: TestResultSearchParam): List<TestResultResponse> {
        val report = TBS_TST_REPORT
        val patient = RBS_PATIENT
        val tstItem = RBS_TST_ITEM
        val item = BTS_ITEM
        val deptItem = BBS_DEPT_TST_ITEM

        // TODO 검사상태&보고상태
        val conditions = mutableListOf(
            report.TST_REQ_DT.between(params.reqStartDt, params.reqEndDt)
        )

        params.reportDt?.let {
            val startOfDay = it.atStartOfDay()
            val endOfDay = it.plusDays(1).atStartOfDay()
            conditions.add(report.DELIVERY_DTIME.between(startOfDay, endOfDay))
        }

        params.directAcctCd?.takeIf { it.isNotBlank() }?.let {
            conditions.add(patient.DIRECT_ACCT_CD.eq(it))
        }

        params.custCd?.takeIf { it.isNotBlank() }?.let {
            conditions.add(patient.CUST_CD.eq(it))
        }

        params.reqNoFrom?.let {
            conditions.add(report.TST_REQ_NO.ge(it))
        }

        params.reqNoTo?.let {
            conditions.add(report.TST_REQ_NO.le(it))
        }

        params.tstCd?.takeIf { it.isNotBlank() }?.let {
            conditions.add(report.TST_CD.eq(it))
        }

        params.deptCd?.takeIf { it.isNotBlank() }?.let {
            conditions.add(deptItem.DEPT_CD.eq(it))
        }

        val query = dslContext
            .select(
                report.TST_REPORT_ID,
                report.TST_REQ_DT,
                report.TST_REQ_NO,

                patient.PAT_NM.`as`("patient_nm"),

                tstItem.TST_STAT1_CD.`as`("tst_status_cd"),
                report.DELIVERY_YN,

                report.TST_CD,
                item.TST_NM,

                report.LIMS_RCV_DTIME,
                report.DELIVERY_DTIME,
                patient.DIRECT_ACCT_CD,
                patient.CUST_CD,
                report.DELIVERER,
                report.ATCH_GRUP_ID,

                report.RST_SHORT,
                report.RST_TXT,
                report.RST_URL
            )
            .from(report)
                .join(patient)
                    .on(report.TST_REQ_DT.eq(patient.TST_REQ_DT))
                    .and(report.TST_REQ_NO.eq(patient.TST_REQ_NO))
                .join(tstItem)
                    .on(report.TST_REQ_DT.eq(tstItem.TST_REQ_DT))
                    .and(report.TST_REQ_NO.eq(tstItem.TST_REQ_NO))
                    .and(report.TST_CD.eq(tstItem.TST_CD))
                .join(item)
                    .on(report.TST_CD.eq(item.TST_CD))
                .join(deptItem)
                    .on(report.TST_CD.eq(deptItem.TST_CD))
            .where(conditions)
            .orderBy(report.TST_REQ_DT.desc(), report.TST_REQ_NO.desc())

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> executeSpec = executeSpec.bind(i, v) }

        return executeSpec
            .fetch()
            .all()
            .map { toTestResultResponse(it) }
            .collectList()
            .awaitSingle()
    }

    override suspend fun deleteById(id: String) {
        testReportDataRepository.deleteById(id)
    }

    private fun toTestResultResponse(row: Map<String, Any>): TestResultResponse {
        return TestResultResponse(
            tstReportId = (row["tst_report_id"] ?: "").toString(),
            tstReqDt = (row["tst_req_dt"] as? LocalDate) ?: LocalDate.now(),
            tstReqNo = (row["tst_req_no"] as? Number)?.toLong() ?: 0L,

            patientNm = (row["patient_nm"] ?: "").toString(),
            tstStatusCd = (row["tst_status_cd"] ?: "").toString(),
            deliveryYn = (row["delivery_yn"] as? Boolean) ?: false,

            tstCd = (row["tst_cd"] ?: "").toString(),
            tstNm = (row["tst_nm"] ?: "").toString(),

            limsRcvDtime = (row["lims_rcv_dtime"] as? LocalDateTime)?.toLocalDate(),
            deliveryDtime = (row["delivery_dtime"] as? LocalDateTime)?.toLocalDate(),

            directAcctCd = (row["direct_acct_cd"] ?: "").toString(),
            custCd = (row["cust_cd"] ?: "").toString(),

            directAcctNm = "",
            custNm = "",

            deliverer = row["deliverer"]?.toString(),
            atchGrupId = row["atch_grup_id"]?.toString(),
            rstShort = row["rst_short"]?.toString(),
            rstTxt = row["rst_txt"]?.toString(),
            rstUrl = row["rst_url"]?.toString()
        )
    }
}
