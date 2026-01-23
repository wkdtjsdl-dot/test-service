package com.idrsys.ailis.sales.application.dto.response

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Billing Request Detail Response DTO
 *
 * Individual test item record for billing
 */
data class BillingRequestResponse(
    val tstReqDt: LocalDate,
    val tstReqNo: String,
    val custCd: String?,
    val custNm: String? = null,
    val patNm: String?,
    val hospChartNo: String?,
    val tstMediumCateCd: String?,
    val tstCd: String,
    val tstNm: String?,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val crcyCd: String?,
    val closingSupval: BigDecimal?,
    val closingAddtax: BigDecimal?,
    val closingSpecialCharge: BigDecimal?,
    val discountRate: BigDecimal?,
    val creator: String,
    val createDtime: LocalDateTime,
    val closingMemo: String?
)
