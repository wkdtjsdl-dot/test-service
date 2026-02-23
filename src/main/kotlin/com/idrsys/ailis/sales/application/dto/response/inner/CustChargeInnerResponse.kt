package com.idrsys.ailis.sales.application.dto.response.inner

import java.time.LocalDate

/**
 * Inner API Response DTO for customer charge information (billing recalculation & TstItem creation)
 */
data class CustChargeInnerResponse(
    val custCd: String,
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val stndPrice: Long?,
    val crcyCd: String,
    val specialCharge: Long,
    val supval: Long?,
    val addtax: Long?,
    val frgnAcctYn: Boolean
)
