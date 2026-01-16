package com.idrsys.ailis.sales.application.dto.query

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Demand with Customer Info Query DTO
 * Used for JOIN query result between sbl_demand and scs_cust_mst
 */
data class DemandWithCustInfo(
    val demandId: String,
    val custCd: String,
    val demandDt: LocalDate,
    val demandStartDt: LocalDate,
    val demandStndDt: LocalDate,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val dscntRate: BigDecimal,
    val slstmtNo: String?,
    val slstmtSendDt: LocalDate?,
    val billPublYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val colledgerId: String?,
    // Customer info from scs_cust_mst
    val custNm: String?
)
