package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 고객수가 승인 정보 Response
 */
data class ChargeApproveResponse(
    val custChargeId: String,
    val custCd: String,
    val custNm: String?,
    val tstCd: String,
    val tstNm: String?,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val specialCharge: Long,
    val stndPrice: Long?,
    val lowestCharge: Long?,
    val supval: Long?,
    val addtax: Long?,
    val remark: String?,
    val apprInfoNo: Long?,
    val currApprSeq: Int?,
    val apprSubmsEmpNo: String?,
    val apprSubmsDtime: LocalDateTime?,
    val lastApprStatCd: String,
    val lastApprStatNm: String?,
    val apprLvlCd: String?,
    val apprLvlNm: String?,
    val approvalLines: List<ApprovalLineInfo>? = null,
)

/**
 * 결재선 정보
 */
data class ApprovalLineInfo(
    val apprInfoId: String,
    val apprSeq: Int,
    val apprPersonEmpNo: String,
    val apprPersonNm: String?,
    val apprStatCd: String,
    val apprStatNm: String?,
    val apprCmplDtime: LocalDateTime?,
    val apprMemo: String?,
)
