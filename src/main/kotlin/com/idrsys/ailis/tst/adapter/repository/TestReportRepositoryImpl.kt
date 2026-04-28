package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.application.required.repository.TestReportRepository
import com.idrsys.ailis.tst.domain.model.TestReport
import com.idrsys.ailis.tst.domain.model.TestReportHst
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem.BTS_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.RbsPatient.RBS_PATIENT
import com.idrsys.ailis.tst.generated.jooq.tables.RbsTstItem.RBS_TST_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.TbsTstReport.TBS_TST_REPORT
import com.idrsys.ailis.tst.generated.jooq.tables.BbsDeptTstItem.BBS_DEPT_TST_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.TbsTstReportHst.TBS_TST_REPORT_HST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * R2DBC Repository Interface
 */
@Repository
interface TestReportDataRepository : CoroutineCrudRepository<TestReport, String>

@Repository
interface TestReportHstDataRepository : CoroutineCrudRepository<TestReportHst, String>

/**
 * 검사결과 보고서 Repository 구현체
 */
@Repository
class TestReportRepositoryImpl(
    private val testReportDataRepository: TestReportDataRepository,
    private val testReportHstDataRepository: TestReportHstDataRepository,
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

    override suspend fun searchTestResults(params: TestResultSearchParam, rerDeptCd: String?): Page<TestResultResponse> {
        val report = TBS_TST_REPORT
        val patient = RBS_PATIENT
        val tstItem = RBS_TST_ITEM
        val item = BTS_ITEM
        val deptItem = BBS_DEPT_TST_ITEM

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

        params.deliveryYn?.takeIf { it.isNotBlank() }?.let { strValue ->
            val booleanValue = (strValue == "Y")
            conditions.add(report.DELIVERY_YN.eq(booleanValue))
        }

        params.patNm?.takeIf { it.isNotBlank() }?.let {
            conditions.add(patient.PAT_NM.like("%$it%"))
        }

        params.hospChartNo?.takeIf { it.isNotBlank() }?.let {
            conditions.add(patient.HOSP_CHART_NO.like("%$it%"))
        }

        val rerYnField =
            if (rerDeptCd != null) {
                DSL.`when`(
                    DSL.exists(
                        DSL.selectOne()
                            .from(deptItem)
                            .where(deptItem.TST_CD.eq(report.TST_CD))
                            .and(deptItem.DEPT_CD.eq(rerDeptCd))
                    ),
                    "Y"
                ).otherwise("N").`as`("rer_yn")
            } else {
                DSL.inline("N").`as`("rer_yn")
            }

        val countQuery = dslContext
            .select(DSL.countDistinct(report.TST_REPORT_ID))
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
                    .on(deptItem.TST_CD.eq(report.TST_CD))
            .where(conditions)

        var countSpec = databaseClient.sql(countQuery.sql)
        countQuery.bindValues.forEachIndexed { i, v -> countSpec = countSpec.bind(i, v) }
        val total = countSpec.fetch().one().map { (it.values.first() as Number).toLong() }.awaitSingle()

        val query = dslContext
            .selectDistinct(
                report.TST_REPORT_ID,
                report.TST_REQ_DT,
                report.TST_REQ_NO,

                patient.PAT_NM.`as`("patient_nm"),
                patient.HOSP_CHART_NO,

                report.TST_CD,
                item.TST_NM,

                patient.DIRECT_ACCT_CD,
                patient.DIRECT_ACCT_BAR,
                patient.CUST_CD,

                report.DELIVERY_YN,
                report.DELIVERY_CD,
                report.DELIVERY_DTIME,
                report.DELIVERER,
                report.ATCH_GRUP_ID,

                report.RST_SHORT,
                report.RST_TXT,
                report.RST_URL,

                rerYnField,
                tstItem.TST_REQ_STAT_CD,
                tstItem.CLOSING_CD,
                tstItem.TST_TAT_DT,
                tstItem.LIMS_TAT_DT,
                report.LIMS_RCV_DTIME
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
                    .on(deptItem.TST_CD.eq(report.TST_CD))
            .where(conditions)
            .orderBy(report.TST_REQ_DT.desc(), report.TST_REQ_NO.desc())
            .limit(params.size)
            .offset(params.page.toLong() * params.size)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { i, v -> executeSpec = executeSpec.bind(i, v) }

        val results = executeSpec
            .fetch()
            .all()
            .map { toTestResultResponse(it) }
            .collectList()
            .awaitSingle()

        return PageImpl(results, PageRequest.of(params.page, params.size), total)
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
            hospChartNo = row["hosp_chart_no"]?.toString(),

            tstCd = (row["tst_cd"] ?: "").toString(),
            tstNm = (row["tst_nm"] ?: "").toString(),

            directAcctCd = (row["direct_acct_cd"] ?: "").toString(),
            custCd = (row["cust_cd"] ?: "").toString(),

            directAcctNm = "",
            custNm = "",

            deliveryYn = (row["delivery_yn"] as Boolean),
            deliveryCd = row["delivery_cd"]?.toString(),
            deliveryDtime = (row["delivery_dtime"] as? LocalDateTime)?.toLocalDate(),
            deliverer = row["deliverer"]?.toString(),
            atchGrupId = row["atch_grup_id"]?.toString(),
            rstShort = row["rst_short"]?.toString(),
            rstTxt = row["rst_txt"]?.toString(),
            rstUrl = row["rst_url"]?.toString(),
            tstReqStatCd = row["tst_req_stat_cd"]?.toString(),
            rerYn = row["rer_yn"]?.toString(),
            closingCd = row["closing_cd"]?.toString(),
            tstTatDt = row["tst_tat_dt"] as? LocalDate,
            limsTatDt = row["lims_tat_dt"] as? LocalDate,
            limsRcvDtime = row["lims_rcv_dtime"] as? LocalDateTime,
            genomeRegNo = run {
                val directAcctCd = (row["direct_acct_cd"] ?: "").toString()
                val tstReqDt = (row["tst_req_dt"] as? LocalDate) ?: LocalDate.now()
                val tstReqNo = (row["tst_req_no"] as? Number)?.toLong() ?: 0L
                if (directAcctCd == "G010000") {
                    val bar = row["direct_acct_bar"]?.toString()?.takeIf { it.length == 15 }
                    if (bar != null) {
                        val datePart = bar.take(8)
                        val numStr = bar.drop(8)
                        val officeCd = numStr.take(3)
                        val seq = numStr.drop(3).padStart(4, '0')
                        "$datePart-$officeCd-$seq"
                    } else {
                        computeGenomeRegNo(tstReqDt, tstReqNo)
                    }
                } else {
                    computeGenomeRegNo(tstReqDt, tstReqNo)
                }
            },
        )
    }

    private fun computeGenomeRegNo(tstReqDt: LocalDate, tstReqNo: Long): String {
        val reqNoStr = tstReqNo.toString()
        if (reqNoStr.length > 7) return ""
        val datePart = tstReqDt.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val officeCd = reqNoStr.take(3)
        val seq = reqNoStr.drop(3).padStart(4, '0')
        return "$datePart-$officeCd-$seq"
    }


    // --- TestReportHst ---
    override suspend fun saveTestReportHistory(entity: TestReportHst): TestReportHst = testReportHstDataRepository.save(entity)
    override suspend fun findTestItemHistoryByTstCd(tstCd: String): Flow<TestReportHst> {
        val table = TBS_TST_REPORT_HST
        val query = dslContext
            .select(table.fields().toList())
            .from(table)
            .where(table.TST_CD.eq(tstCd))
            .orderBy(table.UPDATE_DTIME.desc())

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value: Any? ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toTestReportHst(row) }
            .asFlow()
    }

    private fun toTestReportHst(row: Map<String, Any>): TestReportHst {
        return TestReportHst(
            tstReportHstId = row["tst_report_hst_id"] as String,
            hstCd = row["hst_cd"] as String,
            hstMemo = row["hst_memo"] as String,
            worker = row["worker"] as String,
            workDtime = row["work_dtime"] as LocalDateTime,

            tstReqDt = row["tst_req_dt"] as LocalDate,
            tstReqNo = row["tst_req_no"] as Long,
            tstCd = row["tst_cd"] as String,

            memo = row["memo"] as String?,
            limsRcvDtime = row["lims_rcv_dtime"] as LocalDateTime?,

            rstShort = row["rst_short"] as String?,
            rstTxt = row["rst_txt"] as String?,
            atchGrupId = row["atch_grup_id"] as String?,
            rstUrl = row["rst_url"] as String?,

            deliveryYn = row["delivery_yn"] as Boolean?,
            deliveryCd = row["delivery_cd"] as String?,
            deliveryDtime = row["delivery_dtime"] as LocalDateTime?,
            deliverer = row["deliverer"] as String,

            creator = row["creator"] as String,
            createDtime = row["create_dtime"] as LocalDateTime,
            updater = row["updater"] as String,
            updateDtime = row["update_dtime"] as LocalDateTime
        )
    }
}
