package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.math.BigDecimal

/**
 * 매출목표 조회 응답 (8.1 API)
 * 년도별 고객별 salesTeamCd별 집계 결과
 */
data class SalesTargetResponse(
    val rowId: String,
    @ExcelColumn("연도")
    val year: String,
    @ExcelColumn("고객코드")
    val custCd: String,
    @ExcelColumn("고객명")
    val custNm: String,
    @ExcelColumn("영업팀")
    val salesTeamCd: String,
    @ExcelColumn("담당영업ID")
    val empUserId: String?,
    @ExcelColumn("담당영업")
    val empUserNm: String?,
    @ExcelColumn("매출목표")
    val totalTarget: BigDecimal,
    @ExcelColumn("전년도매출")
    val prevYearSales: BigDecimal,
    @ExcelColumn("목표성장률")
    val targetGrowthRate: BigDecimal?,
)
