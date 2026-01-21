package com.idrsys.ailis.sales.application.dto.response

import java.math.BigDecimal

/**
 * 매출목표 조회 응답 (8.1 API)
 * 년도별 고객별 salesTeamCd별 집계 결과
 */
data class SalesTargetResponse(
    val rowId: String,
    val year: Int,
    val custCd: String,
    val custNm: String,
    val salesTeamCd: String,
    val salesTeamNm: String?,
    val totalTarget: BigDecimal,
    val prevYearSales: BigDecimal,
    val targetGrowthRate: BigDecimal?
)
