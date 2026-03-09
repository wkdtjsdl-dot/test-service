package com.idrsys.ailis.tst.application.dto

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 검사결과 검색 조건
 */
data class TestResultSearchParam(
    val reqStartDt: LocalDate,
    val reqEndDt: LocalDate,
    val reportDt: LocalDate? = null,
    val directAcctCd: String? = null,
    val custCd: String? = null,
    val reqNoFrom: Long? = null,
    val reqNoTo: Long? = null,
    val tstCd: String? = null,
    val deptCd: String? = null,
    val tstStatusCd: String? = null,
    val reportStatusCd: String? = null
)

/**
 * 검사결과 목록 응답 DTO
 */
data class TestResultResponse(
  val tstReportId: String,
  val tstReqDt: LocalDate,
  val tstReqNo: Long,
  val patientNm: String,
  val tstStatusCd: String,
  val deliveryYn: Boolean,
  val tstCd: String,
  val tstNm: String,
  val limsRcvDtime: LocalDate?,
  val deliveryDtime: LocalDate?,
  val directAcctCd: String,
  val custCd: String,
  var directAcctNm: String,
  var custNm: String,
  val deliverer: String?,
  val atchGrupId: String?,
  val rstShort: String?,
  val rstTxt: String?,
  val rstUrl: String?
)

/**
 * 검사결과 보고서 등록 요청 DTO
 */
data class TestReportRegisterRequest(
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
 * 검사결과 보고서 업데이트 요청 DTO
 */
data class TestReportUpdateRequest(
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
 * 배포 요청 DTO
 */
data class DeliveryRequest(
    val deliveryMethod: String
)

/**
 * 배포 결과 DTO
 */
data class DeliveryResult(
    val success: Boolean,
    val deliveryDtime: LocalDateTime,
    val message: String? = null
)

/**
 * 검사결과 이력 DTO
 */
data class TestResultHistoryResponse(
    val tstReportHstId: String,
    val hstCd: String,
    val hstMemo: String,
    val worker: String,
    val workDtime: LocalDateTime,
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val tstCd: String,
    val rstShort: String?,
    val rstTxt: String?,
    val deliveryYn: Boolean
)