package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.adapter.external.BaseServiceClient
import com.idrsys.ailis.tst.adapter.external.ReqServiceClient
import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.mapper.TestReportCommandMapper
import com.idrsys.ailis.tst.application.mapper.TestReportMapper
import com.idrsys.ailis.tst.application.required.external.SalesServicePort
import com.idrsys.ailis.tst.application.required.repository.TestReportRepository
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
    private val baseServiceClient: BaseServiceClient,
    private val salesServiceClient: SalesServicePort,
    private val reqServiceClient: ReqServiceClient,
) : TestReportUseCase {

    @Transactional(readOnly = true)
    override suspend fun searchTestResults(params: TestResultSearchParam): List<TestResultResponse> {
        val rerDeptCd = baseServiceClient
            .getSysCodesByCateCd("RERDPT")
            .firstOrNull()
            ?.etc1

        val results = testReportRepository.searchTestResults(params, rerDeptCd)

        if (results.isEmpty()) return results

        // custCd / directAcctCd 추출
        val custCds = results.map { it.custCd }
        val directAcctCds = results.map { it.directAcctCd }

        val allCustCds = (custCds + directAcctCds)
            .filterNotNull()
            .distinct()

        val custMap = salesServiceClient.findCustNmByCustCd(allCustCds)

        results.forEach { row ->
            row.custNm = row.custCd.let { custMap[it]?.custNm.toString() }
            row.directAcctNm = row.directAcctCd.let { custMap[it]?.custNm.toString() }
        }

        return results
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
        // 1. reportId로 기존 report 정보 가져오기
        val report = testReportRepository.findById(reportId)
            ?: throw NoSuchElementException("Test report not found: $reportId")

        // 2. report를 수정/저장
        val saved = testReportRepository.save(report.apply {
            update(commandMapper.toUpdateCommand(request), adminId, LocalDateTime.now())
        })

        // 3. req-service Inner API 호출(tstItem, patient 수정)
        reqServiceClient.updateTstItemStatus(
            tstReqDt = saved.tstReqDt,
            tstReqNo = saved.tstReqNo,
            tstCd = saved.tstCd,
            statusCd = "RQST_F",
            updater = adminId
        )

        // 4. history 저장
        val hist = testReportMapper.toDomain(report, request.memo ?: "").apply { setAsNew() }
        testReportRepository.saveTestReportHistory(hist)

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

        val filePath = report.atchGrupId // rstFilePath -> atchGrupId 임시 변경
            ?: throw IllegalStateException("No file path for report: $reportId")

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
