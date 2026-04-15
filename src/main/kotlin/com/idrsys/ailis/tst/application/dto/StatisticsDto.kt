package com.idrsys.ailis.tst.application.dto

import com.idrsys.web.excel.ExcelColumn
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

// --- 워크리스트 항목별 이전자료 검색 ---

data class WorklistItemSearchParam(
    val startDt: LocalDate?,
    val endDt: LocalDate?,
    val wrklistCd: String?,
    val tstCd: String?,
    val spcmCd: String?
)

data class WorklistItemResponse(
    @ExcelColumn("등록일자") val reqDt: LocalDate?,
    @ExcelColumn("등록번호") val tstReqNo: Long?,
    @ExcelColumn("거래처코드") val custCd: String?,
    @ExcelColumn("거래처명") val custNm: String?,
    @ExcelColumn("수진자명") val patNm: String?,
    @ExcelColumn("나이") val patAge: Int?,
    @ExcelColumn("성별") val patGender: String?,
    @ExcelColumn("생년월일") val patBday: LocalDate?,
    @ExcelColumn("차트번호") val chartNo: String?,
    @ExcelColumn("상태") val statNm: String?,
    @ExcelColumn("검사코드") val tstCd: String?,
    @ExcelColumn("검사항목명") val tstNm: String?,
    @ExcelColumn("진료과명") val deptNm: String?,
    @ExcelColumn("관리그룹") val mngGroup: String?,
    @ExcelColumn("기타메모") val memo: String?,
    @ExcelColumn("참고사항") val remark: String?,
    @ExcelColumn("오더등록일시") val orderRegDtime: LocalDateTime?,
    @ExcelColumn("림스전송일시") val limsApplyDtime: LocalDateTime?,
    @ExcelColumn("부속거래처명") val subCustNm: String?,
    @ExcelColumn("타기관등록번호") val otherInstRegNo: String?,
    @ExcelColumn("결과보고예정일") val expectedDt: LocalDate?,
    @ExcelColumn("동의서") val agreeYn: Boolean?,
    @ExcelColumn("의뢰서") val reqDocYn: Boolean?,
    @ExcelColumn("지놈예정일") val limsExpectedDt: LocalDate?
)

// --- 임상검사코드 전체항목 ---

data class ClinicalTestCodeSearchParam(
    val tstCd: String?,
    val tstNm: String?,
    val partCd: String?,
    val tstStatCd: String?,
    val useYn: Boolean?,
    val startDt: LocalDate?,
    val endDt: LocalDate?
)

data class ClinicalTestCodeResponse(
    val tstCd: String,
    val useYn: Boolean,
    val tstNm: String,
    val spcmInfo: String?,
    val partNm: String?,
    val tstMethodNm: String?,
    val tstStatNm: String?,
    val tstStatNm2: String?,
    val tstDayweek: String?,
    val tatDay: Int?,
    val tatHour: Int?,
    val outsourceYn: Boolean?,
    val takeQnty: String?,
    val tstGuide: String?,
    val stndPrice: BigDecimal?,
    val supval: BigDecimal?,
    val addtax: BigDecimal?
)

// --- 수가변경 승인처리 ---

data class ChargeChangeApprovalSearchParam(
    val startDt: LocalDate?,
    val endDt: LocalDate?,
    val tstCd: String?,
    val custCd: String?,
    val changeKindCd: String?
)

data class ChargeChangeApprovalResponse(
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val tstCd: String,
    val changeKindNm: String?,
    val memo: String?
)

// --- 수진자별 TAT분석표 ---

data class PatientTatAnalysisSearchParam(
    val startDt: LocalDate,
    val endDt: LocalDate,
    val tstCd: String?
)

data class PatientTatAnalysisResponse(
    val tstEndDtime: LocalDateTime?,
    val expectedDt: LocalDate?,
    val tatDay: Long?,
    val tatHour: Long?,
    val exceedDay: Long?,
    val exceedHour: Long?,
    val memo: String?,
    val limsExpectedDt: LocalDate?
)
