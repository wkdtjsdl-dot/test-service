package com.idrsys.ailis.sales.application.dto.request.estimate

import java.time.LocalDate

/**
 * Estimate Search Parameters
 */
data class EstimateSearchParam(
    val startDt: LocalDate,
    val endDt: LocalDate,
    val docType: String? = null,  // "EST" or "TRN"
    val receiver: String? = null,
    val reference: String? = null
)
