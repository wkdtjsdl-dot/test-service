package com.idrsys.ailis.tst.domain.command

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 검사결과 보고서 생성 Command
 */
data class TestReportCreateCommand(
    val tstReportId: String?,
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val tstCd: String,
    val rstShort: String?,
    val rstTxt: String?,
    val rstFileNm: String?,
    val rstFileExt: String?,
    val rstFilePath: String?,
    val rstUrl: String?,
    val atchGrupId: String?,
    val limsRcvDtime: LocalDateTime?,
    val memo: String?
)

/**
 * 검사결과 보고서 업데이트 Command
 */
data class TestReportUpdateCommand(
    val rstShort: String?,
    val rstTxt: String?,
    val rstFileNm: String?,
    val rstFileExt: String?,
    val rstFilePath: String?,
    val rstUrl: String?,
    val atchGrupId: String?,
    val memo: String?
)

/**
 * 보고서 배포 Command
 */
data class TestReportDeliverCommand(
    val deliveryCd: String
)