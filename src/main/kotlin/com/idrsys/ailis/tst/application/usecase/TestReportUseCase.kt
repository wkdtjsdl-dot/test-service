package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.DeliveryResult
import com.idrsys.ailis.tst.application.dto.TestReportRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestReportUpdateRequest
import com.idrsys.ailis.tst.application.dto.TestResultDetailResponse
import com.idrsys.ailis.tst.application.dto.TestResultExcelResponse
import com.idrsys.ailis.tst.application.dto.TestResultListResponse
import com.idrsys.ailis.tst.application.dto.TestResultSearchParam
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page

/**
 * 검사결과 보고서 UseCase Interface
 */
interface TestReportUseCase {
    /**
     * 검사결과 목록 조회
     */
    suspend fun searchTestResults(params: TestResultSearchParam): Page<TestResultListResponse>

    /**
     * 검사결과 상세 조회
     */
    suspend fun getTestReport(reportId: String): TestResultDetailResponse

    /**
     * 보고서 등록
     */
    suspend fun registerTestReport(request: TestReportRegisterRequest, adminId: String): String

    /**
     * 보고서 수정
     */
    suspend fun updateTestReport(
        reportId: String,
        request: TestReportUpdateRequest,
        adminId: String
    ): TestResultDetailResponse

    /**
     * 보고서 배포
     */
    suspend fun deliverReport(
        reportId: String,
        deliveryCd: String,
        adminId: String
    ): DeliveryResult

    /**
     * 보고서 파일 다운로드
     */
    suspend fun getReportFile(reportId: String): Resource

    /**
     * 보고서 삭제
     */
    suspend fun deleteTestReport(reportId: String, adminId: String)

    /**
     * 검사결과 엑셀 다운로드용 전체 목록 조회
     */
    suspend fun getTestResultExcel(params: TestResultSearchParam): List<TestResultExcelResponse>
}