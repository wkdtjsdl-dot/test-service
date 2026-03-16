package com.idrsys.ailis.tst.application.mapper

import com.idrsys.ailis.tst.application.dto.TestReportRegisterRequest
import com.idrsys.ailis.tst.application.dto.TestReportUpdateRequest
import com.idrsys.ailis.tst.domain.command.TestReportCreateCommand
import com.idrsys.ailis.tst.domain.command.TestReportUpdateCommand
import org.springframework.stereotype.Component
import java.util.*

/**
 * DTO to Command Mapper
 */
@Component
class TestReportCommandMapper {

    /**
     * RegisterRequest를 CreateCommand로 변환
     */
    fun toCreateCommand(request: TestReportRegisterRequest): TestReportCreateCommand {
        return TestReportCreateCommand(
            tstReportId = UUID.randomUUID().toString(),
            tstReqDt = request.tstReqDt,
            tstReqNo = request.tstReqNo,
            tstCd = request.tstCd,
            rstShort = request.rstShort,
            rstTxt = request.rstTxt,
            rstFileNm = request.rstFileNm,
            rstFileExt = request.rstFileExt,
            rstFilePath = request.rstFilePath,
            rstUrl = request.rstUrl,
            atchGrupId = request.atchGrupId,
            limsRcvDtime = request.limsRcvDtime,
            memo = request.memo
        )
    }

    /**
     * UpdateRequest를 UpdateCommand로 변환
     */
    fun toUpdateCommand(request: TestReportUpdateRequest): TestReportUpdateCommand {
        return TestReportUpdateCommand(
            tstReportId = request.tstReportId,
            atchGrupId = request.atchGrupId,
            rstShort = request.rstShort,
            rstTxt = request.rstTxt,
            rstUrl = request.rstUrl,
            memo = request.memo,
            deliveryYn = request.deliveryYn
        )
    }
}