package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.TestResultListResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import com.idrsys.ailis.tst.domain.model.TestReport
import com.idrsys.ailis.tst.domain.model.TestReportHst
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
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
    suspend fun searchTestResults(params: TestResultSearchParam, rerDeptCd: String?): Page<TestResultListResponse>

    /**
     * 검사결과 엑셀 다운로드용 전체 목록 조회
     */
    suspend fun findTestResultsForExcel(params: TestResultSearchParam, rerDeptCd: String?): List<TestResultListResponse>

    /**
     * 보고서 삭제
     */
    suspend fun deleteById(id: String)

    suspend fun saveTestReportHistory(entity: TestReportHst): TestReportHst

    suspend fun findTestItemHistoryByTstCd(tstCd: String): Flow<TestReportHst>
}
