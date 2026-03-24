package com.idrsys.ailis.sales.application.dto.request.chargeapprove

import java.time.LocalDate

/**
 * 고객수가 승인 목록 조회 SearchParam
 */
data class ChargeApproveSearchParam(
    val custCd: String? = null,           // 고객코드
    val tstCd: String? = null,            // 검사코드
    val applyStartDt: LocalDate? = null,  // 적용시작일자 (from)
    val applyEndDt: LocalDate? = null,    // 적용종료일자 (to)
    val lastApprStatCd: String? = null,   // 마지막결재상태코드 (LAST_T, LAST_I, LAST_C)
    val apprLvlCd: String? = null,        // 결재레벨코드
    val myApproval: Boolean = false,      // 내 결재 필터 (true: 내가 결재라인에 포함된 건만)
    val bzoffiCd: String? = null,         // 영업소코드 (SLCP 역할 필터링용)
)
