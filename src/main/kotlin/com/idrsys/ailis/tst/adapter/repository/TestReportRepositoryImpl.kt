package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.application.required.TestReportRepository
import com.idrsys.ailis.tst.domain.model.TestReport
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.DSLContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.lang.Boolean
import java.time.LocalDate

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
        // TODO: 실제 테이블 구조 확인 후 JOIN 쿼리 구현 필요
        // 필요한 테이블:
        // - tbs_tst_report (보고서)
        // - bts_item (검사종목)
        // - tbs_tst_req 또는 유사한 의뢰 테이블 (환자명, 거래처, 병원, 상태 등)
        // - bbs_dept (부서)

        val sql = """
            SELECT
                r.tst_report_id,
                r.tst_req_dt,
                r.tst_req_no,
                r.tst_cd,
                r.rst_short,
                r.rst_txt,
                r.rst_file_nm,
                r.rst_file_path,
                r.rst_url,
                r.delivery_yn,
                r.delivery_dtime
            FROM tst_scm.tbs_tst_report r
            WHERE r.tst_req_dt BETWEEN :reqStartDt AND :reqEndDt
            ORDER BY r.tst_req_dt DESC, r.tst_req_no DESC
        """.trimIndent()

        return databaseClient.sql(sql)
            .bind("reqStartDt", params.reqStartDt)
            .bind("reqEndDt", params.reqEndDt)
            .map { row, _ ->
                TestResultResponse(
                    tstReportId = row.get("tst_report_id", String::class.java) ?: "",
                    tstReqDt = row.get("tst_req_dt", LocalDate::class.java) ?: LocalDate.now(),
                    tstReqNo = row.get("tst_req_no", java.lang.Long::class.java)?.toLong() ?: 0L,
                    patientNm = "", // TODO: JOIN 필요
                    tstCd = row.get("tst_cd", String::class.java) ?: "",
                    tstNm = "", // TODO: JOIN 필요 (bts_item)
                    tstStatusCd = "", // TODO: JOIN 필요
                    tstStatusNm = null,
                    reportStatusCd = "", // TODO: JOIN 필요
                    reportStatusNm = null,
                    custNm = "", // TODO: JOIN 필요
                    hospNm = "", // TODO: JOIN 필요
                    deptNm = "", // TODO: JOIN 필요
                    reportDt = row.get("delivery_dtime", java.time.LocalDateTime::class.java)?.toLocalDate(),
                    deliveryDt = row.get("delivery_dtime", java.time.LocalDateTime::class.java)?.toLocalDate(),
                    testerNm = null,
                    reporterNm = null,
                    reportSeq = null,
                    rstShort = row.get("rst_short", String::class.java),
                    rstTxt = row.get("rst_txt", String::class.java),
                    rstFileNm = row.get("rst_file_nm", String::class.java),
                    rstFilePath = row.get("rst_file_path", String::class.java),
                    rstUrl = row.get("rst_url", String::class.java),
                    deliveryYn = (row.get("delivery_yn", Boolean::class.java) ?: false) as kotlin.Boolean
                )
            }
            .all()
            .collectList()
            .awaitFirstOrNull() ?: emptyList()
    }

    override suspend fun deleteById(id: String) {
        testReportDataRepository.deleteById(id)
    }
}