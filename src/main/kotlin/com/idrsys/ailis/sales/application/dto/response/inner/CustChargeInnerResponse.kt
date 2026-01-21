package com.idrsys.ailis.sales.application.dto.response.inner

import java.time.LocalDate

/**
 * Inner API Response DTO for customer charge information (billing recalculation)
 */
data class CustChargeInnerResponse(
    val custCd: String,
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val stndPrice: Long?,
    val supval: Long?,
    val addtax: Long?
)
