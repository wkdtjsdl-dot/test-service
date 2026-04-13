package com.idrsys.ailis.tst.application.dto

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
    val limsTransDtime: LocalDateTime?,
    val subCustNm: String?,
    val otherInstRegNo: String?,
    val expectedDt: LocalDate?,
    val agreeYn: Boolean?,
    val reqDocYn: Boolean?,
    val limsExpectedDt: LocalDate?
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
