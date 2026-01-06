package com.idrsys.ailis.sales.application.dto.request.billing

import java.time.LocalDate

/**
 * Demand Search Parameters
 *
 * Parameters for querying demand list (settled or unsettled)
 */
data class DemandSearchParam(
    val demandType: DemandType,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val custCd: String? = null,
    val branchCd: String? = null
)

enum class DemandType {
    SETTLED,    // 이전청구 (마감완료)
    UNSETTLED   // 미청구
}
