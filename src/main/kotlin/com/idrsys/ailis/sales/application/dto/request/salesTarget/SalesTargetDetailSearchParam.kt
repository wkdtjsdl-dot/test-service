package com.idrsys.ailis.sales.application.dto.request.salesTarget

/**
 * 매출목표 상세 조회 파라미터 (8.2 API)
 * custCd별 년월별 salesTeamCd별 집계 조회
 */
data class SalesTargetDetailSearchParam(
    val year: String,
    val custCd: String
)
