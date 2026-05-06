package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestResultDetailResponse
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

    fun toDetailResponse(entity: TestReport): TestResultDetailResponse {
        return TestResultDetailResponse(
            tstReportId = entity.tstReportId ?: "",
            tstReqDt = entity.tstReqDt,
            tstReqNo = entity.tstReqNo,
            tstCd = entity.tstCd,
            rstShort = entity.rstShort,
            rstTxt = entity.rstTxt,
            rstUrl = entity.rstUrl,
            atchGrupId = entity.atchGrupId,
            deliveryYn = entity.deliveryYn,
            memo = entity.memo,
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