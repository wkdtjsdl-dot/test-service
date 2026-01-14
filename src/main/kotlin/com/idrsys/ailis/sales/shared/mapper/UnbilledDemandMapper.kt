package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceUnbilledDemandSummary
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Map tst-service unbilled demand data to DemandResponse
 *
 * Since this is unbilled (before closing), some fields are set to null or default values
 */
fun TstServiceUnbilledDemandSummary.toDemandResponse(
    searchStartDt: LocalDate
): DemandResponse {
    return DemandResponse(
        demandId = null,                        // No ID before closing
        custCd = this.custCd,
        custNm = this.custNm,
        branchNm = this.branchNm,
        demandDt = LocalDate.now(),             // Query date
        demandStartDt = searchStartDt,          // Search start date
        demandStndDt = LocalDate.now(),         // Demand standard date (current date)
        stndPrice = this.stndPrice,
        supval = this.supval,
        addtax = this.addtax,
        demandCharge = this.demandCharge,
        dscntRate = BigDecimal.ZERO,            // Calculated after closing
        slstmtNo = null,                        // No statement number before closing
        slstmtSendDt = null,                    // No send date before closing
        billPublYn = false,
        creator = "-",                          // Not yet created
        createDtime = LocalDateTime.now(),      // Query time
        colledgerId = null,                     // Not set before closing
        createdRequestCount = this.requestCount
    )
}

/**
 * Map req-service unbilled demand data to DemandResponse
 *
 * Since this is unbilled (before closing), some fields are set to null or default values
 */
fun ReqServiceUnbilledDemandSummary.toDemandResponse(
    searchStartDt: LocalDate,
    searchEndDt: LocalDate,
): DemandResponse {
    return DemandResponse(
        demandId = null,                        // No ID before closing
        custCd = this.directAcctCd,
        custNm = this.custNm,
        branchNm = this.branchNm,
        demandDt = LocalDate.now(),             // Query date
        demandStartDt = searchStartDt,          // Search start date
        demandStndDt = searchEndDt,             // Demand standard date (Search end date)
        stndPrice = this.stndPrice,
        supval = this.supval,
        addtax = this.addtax,
        demandCharge = this.demandCharge,
        dscntRate = BigDecimal.ZERO,            // Calculated after closing
        slstmtNo = null,                        // No statement number before closing
        slstmtSendDt = null,                    // No send date before closing
        billPublYn = false,
        creator = "-",                          // Not yet created
        createDtime = LocalDateTime.now(),      // Query time
        colledgerId = null,                     // Not set before closing
        createdRequestCount = this.requestCount
    )
}
