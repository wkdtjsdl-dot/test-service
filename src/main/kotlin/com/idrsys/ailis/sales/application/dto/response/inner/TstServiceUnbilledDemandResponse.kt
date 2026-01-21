package com.idrsys.ailis.sales.application.dto.response.inner

import java.math.BigDecimal

/**
 * Unbilled demand summary from tst-service (Inner API)
 */
data class TstServiceUnbilledDemandSummary(
    val custCd: String,
    val custNm: String?,
    val branchNm: String?,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val requestCount: Int
)

/**
 * Page response for unbilled demand summaries from tst-service
 */
data class TstServiceUnbilledDemandPage(
    val content: List<TstServiceUnbilledDemandSummary>,
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val number: Int
)
