package com.idrsys.ailis.sales.application.dto.query

import java.math.BigDecimal

/**
 * 매출목표 조회 쿼리 결과 (8.1 API)
 * 년도별 고객별 salesTeamCd별 집계 결과
 */
data class SalesTargetQuery(
    val salesTargetId: String,
    val salesYear: String,
    val custCd: String,
    val custNm: String,
    val salsTeamCd: String,
    val salsTeamNm: String?,
    val totalTarget: BigDecimal,
    val prevYearSales: BigDecimal
)

/**
 * 매출목표 상세 조회 쿼리 결과 (8.2 API)
 * custCd별 년월별 salesTeamCd별 집계 결과
 */
data class SalesTargetDetailQuery(
    val salesTargetId: String,
    val salesYear: String,
    val salesMonth: String,
    val custCd: String,
    val custNm: String,
    val salsTeamCd: String,
    val salsTeamNm: String?,
    val monthlyTarget: BigDecimal,
    val prevYearSales: BigDecimal
)
