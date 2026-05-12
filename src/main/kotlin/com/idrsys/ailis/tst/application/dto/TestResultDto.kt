package com.idrsys.ailis.tst.application.dto

import com.idrsys.web.excel.ExcelColumn
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
  val deliveryYn: String? = null,
  val patNm: String? = null,
  val hospChartNo: String? = null,
  val wrklstCds: List<String>? = null,
  val page: Int = 0,
  val size: Int = 40,
)

/**
 * 검사결과 목록 응답 DTO (리스트 표시용 lean 타입)
 */
data class TestResultListResponse(
  val tstReportId: String,
  val tstReqDt: LocalDate,
  val tstReqNo: Long,
  val patientNm: String?,
  val hospChartNo: String?,
  val tstCd: String,
  val tstNm: String?,
  val directAcctCd: String?,
  val custCd: String?,
  var directAcctNm: String?,
  var custNm: String?,
  val deliveryYn: Boolean,
  val atchGrupId: String?,
  val genomeRegNo: String?,
  val tstReqStatCd: String?,
  var tstReqStatNm: String? = null,
  var rerYn: String? = null,
  val closingCd: String? = null,
  val tstTatDt: LocalDate? = null,
  val limsTatDt: LocalDate? = null,
  val limsRcvDtime: LocalDateTime? = null,
)

/**
 * 검사결과 상세 응답 DTO (row 클릭 시 detail API 전용)
 */
data class TestResultDetailResponse(
  val tstReportId: String,
  val tstReqDt: LocalDate,
  val tstReqNo: Long,
  val tstCd: String,
  val rstShort: String?,
  val rstTxt: String?,
  val rstUrl: String?,
  val atchGrupId: String?,
  val deliveryYn: Boolean,
  val memo: String?,
)

/**
 * 검사결과 엑셀 다운로드 응답 DTO
 */
data class TestResultExcelResponse(
    @ExcelColumn("상태")
    val reqStatNm: String?,
    @ExcelColumn("의뢰일자")
    val tstReqDt: LocalDate,
    @ExcelColumn("의뢰번호")
    val tstReqNo: String,
    @ExcelColumn("수진자명")
    val patientNm: String?,
    @ExcelColumn("차트번호")
    val hospChartNo: String?,
    @ExcelColumn("검사코드")
    val tstCd: String,
    @ExcelColumn("검사명")
    val tstNm: String?,
    @ExcelColumn("거래처명")
    val directAcctNm: String?,
    @ExcelColumn("재수탁거래처")
    val custNm: String?,
    @ExcelColumn("타기관등록번호")
    val genomeRegNo: String?,
    @ExcelColumn("배포여부")
    val deliveryYn: String?,
    @ExcelColumn("결과보고예정일")
    val tstTatDt: LocalDate?,
    @ExcelColumn("지놈예정일")
    val limsTatDt: LocalDate?,
    @ExcelColumn("검사종료일")
    val limsRcvDtime: LocalDateTime?,
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
    val tstReportId: String,
    val atchGrupId: String?,
    val rstShort: String?,
    val rstTxt: String?,
    val rstUrl: String?,
    val memo: String?,
    val deliveryYn: Boolean?
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