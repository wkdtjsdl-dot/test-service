package com.idrsys.ailis.sales.application.dto.response.inner

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Billing request detail from req-service (Inner API)
 */
data class ReqServiceBillingRequestDetail(
    val tstReqDt: LocalDate,
    val tstReqNo: Long,
    val custCd: String?,
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
