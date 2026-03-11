package com.idrsys.ailis.sales.application.dto.request.billing

import java.time.LocalDate

/**
 * Demand Search Parameters
 *
 * Parameters for querying demand list (settled or unsettled)
 */
data class DemandSearchParam(
    val clcdYn: CLCD,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val custCd: String? = null,
    val branchCd: String? = null,
    val frgnAcctYn: Boolean? = null  // null = 전체 조회 (국내+해외)
)

enum class CLCD {
    CLCD_Y,    // 마감완료(이전청구)
    CLCD_N   // 마감전(미청구)
}
