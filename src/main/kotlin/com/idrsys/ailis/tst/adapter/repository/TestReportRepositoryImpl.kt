package com.idrsys.ailis.tst.adapter.repository

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.application.required.TestReportRepository
import com.idrsys.ailis.tst.domain.model.TestReport
import com.idrsys.ailis.tst.generated.jooq.tables.BtsItem.BTS_ITEM
import com.idrsys.ailis.tst.generated.jooq.tables.TbsTstReport.TBS_TST_REPORT
import kotlinx.coroutines.reactive.awaitFirstOrNull
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
        // jOOQ + R2DBC를 사용한 타입 안전 쿼리
        // 필요한 데이터 출처:
        // 1. tbs_tst_report (검사결과 보고서) + bts_item (검사종목) - 현재 쿼리에서 JOIN
        // 2. req-service Inner API (의뢰 정보) - 환자명, 거래처, 병원, 검사상태, 보고상태, 부서코드
        // 3. base-service Inner API (부서명, 시스템 코드명) - Service 레이어에서 병합

        val report = TBS_TST_REPORT
        val item = BTS_ITEM

        // jOOQ DSL로 쿼리 생성
        val query = dslContext
            .select(
                report.TST_REPORT_ID.`as`("tst_report_id"),
                report.TST_REQ_DT.`as`("tst_req_dt"),
                report.TST_REQ_NO.`as`("tst_req_no"),
                report.TST_CD.`as`("tst_cd"),
                item.TST_NM.`as`("tst_nm"),
                report.RST_SHORT.`as`("rst_short"),
                report.RST_TXT.`as`("rst_txt"),
                report.RST_FILE_NM.`as`("rst_file_nm"),
                report.RST_FILE_PATH.`as`("rst_file_path"),
                report.RST_URL.`as`("rst_url"),
                report.DELIVERY_YN.`as`("delivery_yn"),
                report.DELIVERY_DTIME.`as`("delivery_dtime")
            )
            .from(report)
            .leftJoin(item).on(report.TST_CD.eq(item.TST_CD))
            .where(report.TST_REQ_DT.between(params.reqStartDt, params.reqEndDt))
            .orderBy(report.TST_REQ_DT.desc(), report.TST_REQ_NO.desc())

        // DatabaseClient로 SQL 실행 (기존 패턴 - renderInlined 방식)
        return databaseClient.sql(dslContext.renderInlined(query))
            .map { row, _ ->
                TestResultResponse(
                    tstReportId = row.get("tst_report_id", String::class.java) ?: "",
                    tstReqDt = row.get("tst_req_dt", LocalDate::class.java) ?: LocalDate.now(),
                    tstReqNo = row.get("tst_req_no", java.lang.Long::class.java)?.toLong() ?: 0L,
                    // TODO: req-service Inner API로 조회 필요
                    patientNm = "",
                    tstCd = row.get("tst_cd", String::class.java) ?: "",
                    tstNm = row.get("tst_nm", String::class.java) ?: "",
                    // TODO: req-service Inner API로 조회 필요
                    tstStatusCd = "",
                    tstStatusNm = null,
                    reportStatusCd = "",
                    reportStatusNm = null,
                    custNm = "",
                    hospNm = "",
                    deptCd = null, // TODO: req-service Inner API로 조회 필요
                    deptNm = "", // base-service Inner API로 조회 (Service 레이어에서 병합)
                    reportDt = row.get("delivery_dtime", LocalDateTime::class.java)?.toLocalDate(),
                    deliveryDt = row.get("delivery_dtime", LocalDateTime::class.java)?.toLocalDate(),
                    testerNm = null,
                    reporterNm = null,
                    reportSeq = null, // TODO: 보고 차수 로직 구현 필요
                    rstShort = row.get("rst_short", String::class.java),
                    rstTxt = row.get("rst_txt", String::class.java),
                    rstFileNm = row.get("rst_file_nm", String::class.java),
                    rstFilePath = row.get("rst_file_path", String::class.java),
                    rstUrl = row.get("rst_url", String::class.java),
                    deliveryYn = (row.get("delivery_yn") as? Boolean) ?: false
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