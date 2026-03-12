package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.domain.model.TestReport
import com.idrsys.ailis.tst.domain.model.TestReportHst
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

/**
 * TestReport Entity to DTO Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TestReportMapper {

    /**
     * TestReport 엔티티를 TestResultResponse DTO로 변환
     *
     * Note: 실제로는 JOIN된 데이터가 필요하므로, 이 메서드는 제한적으로 사용됨
     * Repository에서 직접 TestResultResponse를 생성하는 것이 더 효율적
     */
    fun toResponse(entity: TestReport): TestResultResponse {
        return TestResultResponse(
            tstReportId = entity.tstReportId ?: "",
            tstReqDt = entity.tstReqDt,
            tstReqNo = entity.tstReqNo,

            patientNm = "",

            tstCd = entity.tstCd,
            tstNm = "",

            directAcctCd = "",
            custCd = "",

            directAcctNm = "",
            custNm = "",

            deliveryYn = entity.deliveryYn,
            deliveryCd = entity.deliveryCd,
            deliveryDtime = entity.deliveryDtime?.toLocalDate(),
            deliverer = entity.deliverer,

            atchGrupId = entity.atchGrupId,

            rstShort = entity.rstShort,
            rstTxt = entity.rstTxt,
            tstReqStatCd = "",
            rstUrl = entity.rstUrl
        )
    }

    // --- TestReportHst ---
    @Mappings(
        Mapping(target = "tstReportHstId", ignore = true),
        Mapping(target = "hstCd", source = "testReport.tstReportId"),
        Mapping(target = "hstMemo", source = "updateReason"),
        Mapping(target = "worker", source = "testReport.updater"),
        Mapping(target = "workDtime", source = "testReport.updateDtime")
    )
    fun toDomain(testReport: TestReport, updateReason: String): TestReportHst

}