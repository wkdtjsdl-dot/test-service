package com.idrsys.ailis.sales.application.dto.query

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Modifiable demand info for monthly billing recalculation
 * slstmt_no IS NULL demands for a given period
 */
data class DemandMonthlyInfo(
    val demandId: String,
    val custCd: String,
    val demandType: String,
    val crcyCd: String?,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val colledgerId: String?
)

/**
 * Demand with Customer Info Query DTO
 * Used for JOIN query result between sbl_demand and scs_cust_mst
 */
data class DemandWithCustInfo(
    val demandId: String,
    val custCd: String,
    val demandDt: LocalDate,
    val demandStartDt: LocalDate,
    val demandEndDt: LocalDate,
    val demandCreatorEmpNo: String?,
    val demandCreateDtime: LocalDateTime,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal,
    val demandCharge: BigDecimal,
    val dscntRate: BigDecimal,
    val slstmtNo: String?,
    val slstmtSendDt: LocalDate?,
    val billPublYn: Boolean,
    val invcRecpEmailAddr: String?,
    val demandMemo: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val colledgerId: String?,
    // Customer info from scs_cust_mst
    val custNm: String?,
    val bzoffiCd: String?,
    val sapCustCd: String?,
    val crcyCd: String?,
    val frgnCrcyAmt: BigDecimal? = null,
    val demandType: String? = null
)
