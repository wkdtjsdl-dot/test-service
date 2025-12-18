package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestResultResponse
import com.idrsys.ailis.tst.domain.model.TestReport
import org.springframework.stereotype.Component

/**
 * TestReport Entity to DTO Mapper
 */
@Component
class TestReportMapper {

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
            patientNm = "", // TODO: req-service Inner API로 조회 필요
            tstCd = entity.tstCd,
            tstNm = "", // TODO: bts_item JOIN 또는 조회 필요
            tstStatusCd = "", // TODO: req-service Inner API로 조회 필요
            tstStatusNm = null,
            reportStatusCd = "", // TODO: req-service Inner API로 조회 필요
            reportStatusNm = null,
            custNm = "", // TODO: req-service Inner API로 조회 필요
            hospNm = "", // TODO: req-service Inner API로 조회 필요
            deptCd = null, // TODO: req-service Inner API로 조회 필요
            deptNm = "", // TODO: base-service Inner API로 조회 필요 (deptCd 기반)
            reportDt = entity.deliveryDtime?.toLocalDate(),
            deliveryDt = entity.deliveryDtime?.toLocalDate(),
            testerNm = null,
            reporterNm = null,
            reportSeq = null,
            rstShort = entity.rstShort,
            rstTxt = entity.rstTxt,
            rstFileNm = entity.rstFileNm,
            rstFilePath = entity.rstFilePath,
            rstUrl = entity.rstUrl,
            deliveryYn = entity.deliveryYn
        )
    }
}