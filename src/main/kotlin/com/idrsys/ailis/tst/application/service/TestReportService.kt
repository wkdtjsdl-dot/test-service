package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestReportCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestReportMapper
import com.idrsys.ailis.tst.application.required.TestReportRepository
import com.idrsys.ailis.tst.application.usecase.TestReportUseCase
import com.idrsys.ailis.tst.domain.model.TestReport
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths
import java.time.LocalDateTime

/**
 * 검사결과 보고서 Service
 */
@Service
class TestReportService(
    private val testReportRepository: TestReportRepository,
    private val testReportMapper: TestReportMapper,
    private val commandMapper: TestReportCommandMapper,
    private val baseServiceClient: com.idrsys.ailis.tst.adapter.external.BaseServiceClient
) : TestReportUseCase {

    @Transactional(readOnly = true)
    override suspend fun searchTestResults(params: TestResultSearchParam): List<TestResultResponse> {
        // 1. Repository에서 기본 검사결과 조회 (tbs_tst_report + bts_item JOIN)
        val reports = testReportRepository.searchTestResults(params)

        // TODO: req-service Inner API로 의뢰 정보 조회
        // req-service가 구현되면 아래 주석 해제하여 사용
        // - 환자명(patientNm), 거래처(custNm), 병원(hospNm), 부서코드(deptCd)
        // - 검사상태(tstStatusCd), 보고상태(reportStatusCd)
        //
        // val requestKeys = reports.map { TestRequestKey(it.tstReqDt, it.tstReqNo) }.distinct()
        // val requestInfos = reqServiceClient.getTestRequestsByKeys(requestKeys)

        // 2. base-service Inner API로 부서명 조회
        // req-service에서 deptCd를 받아온 후 부서명 조회
        // 현재는 deptCd가 null이므로 실행되지 않음
        val deptCds = reports.mapNotNull { it.deptCd }.distinct()
        val deptNames = if (deptCds.isNotEmpty()) {
            baseServiceClient.getDepartmentsByDeptCds(deptCds)
        } else {
            emptyMap()
        }

        // 3. 데이터 병합
        return reports.map { report ->
            report.copy(
                // base-service에서 조회한 부서명으로 업데이트
                deptNm = report.deptCd?.let { deptNames[it] } ?: report.deptNm

                // TODO: req-service 연동 시 아래 주석 해제
                // val requestInfo = requestInfos[TestRequestKey(report.tstReqDt, report.tstReqNo)]
                // patientNm = requestInfo?.patientNm ?: report.patientNm,
                // custNm = requestInfo?.custNm ?: report.custNm,
                // hospNm = requestInfo?.hospNm ?: report.hospNm,
                // deptCd = requestInfo?.deptCd ?: report.deptCd,
                // tstStatusCd = requestInfo?.tstStatusCd ?: report.tstStatusCd,
                // tstStatusNm = requestInfo?.tstStatusNm,
                // reportStatusCd = requestInfo?.reportStatusCd ?: report.reportStatusCd,
                // reportStatusNm = requestInfo?.reportStatusNm
            )
        }
    }

    @Transactional(readOnly = true)
    override suspend fun getTestReport(reportId: String): TestResultResponse {
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")
        return testReportMapper.toResponse(report)
    }

    @Transactional
    override suspend fun registerTestReport(
        request: TestReportRegisterRequest,
        adminId: String
    ): String {
        // 중복 체크
        val existing = testReportRepository.findByReqAndTestCode(
            request.tstReqDt,
            request.tstReqNo,
            request.tstCd
        )
        if (existing != null) {
            throw IllegalStateException(
                "Test report already exists for req: ${request.tstReqDt}/${request.tstReqNo}/${request.tstCd}"
            )
        }

        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val report = TestReport.create(command, adminId, now)

        val saved = testReportRepository.save(report)
        return saved.tstReportId ?: throw IllegalStateException("Failed to save test report")
    }

    @Transactional
    override suspend fun updateTestReport(
        reportId: String,
        request: TestReportUpdateRequest,
        adminId: String
    ): TestResultResponse {
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        report.update(command, adminId, now)

        val saved = testReportRepository.save(report)
        return testReportMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deliverReport(
        reportId: String,
        deliveryCd: String,
        adminId: String
    ): DeliveryResult {
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")

        val now = LocalDateTime.now()
        report.deliver(
            deliveryCd = deliveryCd,
            deliverer = adminId,
            deliveryDtime = now,
            updater = adminId,
            updateDtime = now
        )

        testReportRepository.save(report)

        return DeliveryResult(
            success = true,
            deliveryDtime = now,
            message = "Report delivered successfully"
        )
    }

    @Transactional(readOnly = true)
    override suspend fun getReportFile(reportId: String): Resource {
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")

        val filePath = report.rstFilePath
            ?: throw IllegalStateException("No file path for report: $reportId")

        // TODO: 실제 파일 저장 경로 설정 필요 (application.yml 등에서 설정)
        val fileSystemPath = Paths.get("/var/data/reports", filePath)
        val resource = FileSystemResource(fileSystemPath)

        if (!resource.exists()) {
            throw NoSuchElementException("Report file not found: $filePath")
        }

        return resource
    }

    @Transactional
    override suspend fun deleteTestReport(reportId: String, adminId: String) {
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")

        // 실제로는 soft delete를 구현하거나 이력 테이블로 이동해야 할 수 있음
        testReportRepository.deleteById(reportId)
    }
}