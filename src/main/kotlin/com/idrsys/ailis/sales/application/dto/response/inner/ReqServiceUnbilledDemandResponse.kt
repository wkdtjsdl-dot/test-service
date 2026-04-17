package com.idrsys.ailis.sales.application.dto.response.inner

import java.math.BigDecimal

/**
 * Unbilled demand summary from req-service (Inner API)
 */
data class ReqServiceUnbilledDemandSummary(
    val directAcctCd: String,
    val custNm: String?,
    val branchNm: String?,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val requestCount: Int,
    val tstReqDivCd: String? = null,
    val crcyCd: String? = null
)
