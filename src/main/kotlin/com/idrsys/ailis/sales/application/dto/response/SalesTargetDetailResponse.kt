package com.idrsys.ailis.sales.application.dto.response

import java.math.BigDecimal

/**
 * 매출목표 상세 조회 응답 (8.2 API)
 * custCd별 년월별 salesTeamCd별 집계 결과
 */
data class SalesTargetDetailResponse(
    val salesTargetId: String,
    val year: Int,
    val custCd: String,
    val custNm: String,
    val salesTeamCd: String,
    val salesTeamNm: String?,
    val month: Int,
    val monthlyTarget: BigDecimal,
    val prevYearSales: BigDecimal
)
