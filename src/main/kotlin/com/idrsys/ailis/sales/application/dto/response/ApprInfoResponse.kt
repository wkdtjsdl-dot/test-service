package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

/**
 * 결재정보 Inner API Response
 * - apprInfoNo로 결재라인을 조회할 때 사용
 */
data class ApprInfoResponse(
    val apprInfoNo: Long,
    val apprSeq: Int,
    val apprDocTypeCd: String,
    val apprPersonEmpNo: String,
    val apprStatCd: String,
    val apprCmplDtime: LocalDateTime?,
    val apprMemo: String?
)
