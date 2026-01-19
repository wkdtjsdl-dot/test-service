package com.idrsys.ailis.sales.application.dto.request.salesTarget

/**
 * 매출목표 조회 파라미터 (8.1 API)
 * 년도별 고객별 salesTeamCd별 집계 조회
 */
data class SalesTargetSearchParam(
    val year: Int,
    val directAcctCd: String? = null
)
