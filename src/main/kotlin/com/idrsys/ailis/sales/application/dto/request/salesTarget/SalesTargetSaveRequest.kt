package com.idrsys.ailis.sales.application.dto.request.salesTarget

import java.math.BigDecimal

/**
 * 매출목표 저장 요청 (8.3 API)
 */
data class SalesTargetSaveRequest(
    val year: String,
    val custCd: String,
    val monthlyTargets: List<MonthlyTargetItem>
)

data class MonthlyTargetItem(
    val month: String,
    val salsTeamCd: String,
    val monthlyTarget: BigDecimal,
    val prevYearSales: BigDecimal = BigDecimal.ZERO
)
