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

            patientNm = "",

            tstStatusCd = "",
            deliveryYn = entity.deliveryYn,

            tstCd = entity.tstCd,
            tstNm = "",

            limsRcvDtime = entity.limsRcvDtime?.toLocalDate(),
            deliveryDtime = entity.deliveryDtime?.toLocalDate(),

            directAcctCd = "",
            custCd = "",

            directAcctNm = "",
            custNm = "",

            deliverer = entity.deliverer,

            atchGrupId = entity.atchGrupId,

            rstShort = entity.rstShort,
            rstTxt = entity.rstTxt,
            rstUrl = entity.rstUrl
        )
    }
}