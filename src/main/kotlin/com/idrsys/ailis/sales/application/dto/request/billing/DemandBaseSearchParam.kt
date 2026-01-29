package com.idrsys.ailis.sales.application.dto.request.billing

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

/**
 * Demand Base Search Parameters
 *
 * Common parameters for querying demand list without frgnAcctYn.
 * Used by API endpoints that determine frgnAcctYn internally.
 */
data class DemandBaseSearchParam(
    @field:Schema(description = "마감여부", example = "CLCD_N")
    val clcdYn: CLCD,
    @field:Schema(description = "시작일", example = "2024-01-01")
    val startDt: LocalDate,
    @field:Schema(description = "종료일", example = "2024-01-31")
    val endDt: LocalDate,
    @field:Schema(description = "고객코드", example = "CUST001")
    val custCd: String? = null,
    @field:Schema(description = "지점코드", example = "BR001")
    val branchCd: String? = null,
)

/**
 * Extension function to convert DemandBaseSearchParam to DemandSearchParam
 *
 * @param frgnAcctYn Foreign account filter (null = all, true = foreign only, false = domestic only)
 */
fun DemandBaseSearchParam.toDemandSearchParam(frgnAcctYn: Boolean?) = DemandSearchParam(
    clcdYn = this.clcdYn,
    startDt = this.startDt,
    endDt = this.endDt,
    custCd = this.custCd,
    branchCd = this.branchCd,
    frgnAcctYn = frgnAcctYn
)
