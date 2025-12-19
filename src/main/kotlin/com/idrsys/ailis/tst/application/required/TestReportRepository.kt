package com.idrsys.ailis.tst.application.required

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.domain.model.TestReport
import java.time.LocalDate

/**
 * 검사결과 보고서 Repository Interface
 */
interface TestReportRepository {
    /**
     * 보고서 저장
     */
    suspend fun save(testReport: TestReport): TestReport

    /**
     * 보고서 ID로 조회
     */
    suspend fun findById(id: String): TestReport?

    /**
     * 의뢰정보와 검사코드로 조회
     */
    suspend fun findByReqAndTestCode(
        tstReqDt: LocalDate,
        tstReqNo: Long,
        tstCd: String
    ): TestReport?

    /**
     * 검사결과 목록 검색 (JOIN 포함)
     */
    suspend fun searchTestResults(params: TestResultSearchParam): List<TestResultResponse>

    /**
     * 보고서 삭제
     */
    suspend fun deleteById(id: String)
}