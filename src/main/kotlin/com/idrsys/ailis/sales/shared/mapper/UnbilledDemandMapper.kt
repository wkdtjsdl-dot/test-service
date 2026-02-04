package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceUnbilledDemandSummary
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Map req-service unbilled demand data to DemandResponse
 *
 * Since this is unbilled (before closing), some fields are set to null or default values
 * @param custNm Customer name from sales-service DB (scs_cust_mst) via batch query
 * @param bzoffiCd Business office code from sales-service DB (scs_cust_mst)
 * @param sapCustCd SAP customer code from sales-service DB (scs_cust_mst)
 */
fun ReqServiceUnbilledDemandSummary.toDemandResponse(
    searchStartDt: LocalDate,
    searchEndDt: LocalDate,
    custNm: String? = null,
    invcRecpEmailYn: Boolean = false,
    invcRecpEmailAddr: String? = null,
    bzoffiCd: String? = null,
    sapCustCd: String? = null
): DemandResponse {
    val dscntRate = if (stndPrice > BigDecimal.ZERO) {
        stndPrice.subtract(demandCharge)
            .divide(stndPrice, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
            .setScale(2, RoundingMode.HALF_UP)
    } else {
        null
    }

    return DemandResponse(
        demandId = null,                        // No ID before closing
        custCd = this.directAcctCd,
        custNm = custNm,                        // From sales-service DB (scs_cust_mst)
        branchNm = this.branchNm,
        demandDt = LocalDate.now(),             // Query date
        demandStartDt = searchStartDt,          // Search start date
        demandEndDt = searchEndDt,             // Demand standard date (Search end date)
        demandCreatorEmpNo = null,
        demandCreateDtime = LocalDateTime.now(),
        stndPrice = this.stndPrice,
        supval = this.supval,
        addtax = this.addtax,
        demandCharge = this.demandCharge,
        dscntRate = dscntRate,            // Calculated after closing
        slstmtNo = null,                        // No statement number before closing
        slstmtSendDt = null,                    // No send date before closing
        billPublYn = invcRecpEmailYn,
        invcRecpEmailAddr = invcRecpEmailAddr,
        bzoffiCd = bzoffiCd,                    // From sales-service DB (scs_cust_mst)
        sapCustCd = sapCustCd,                  // From sales-service DB (scs_cust_mst)
        creator = "-",                          // Not yet created
        createDtime = LocalDateTime.now(),      // Query time
        colledgerId = null,                     // Not set before closing
        createdRequestCount = this.requestCount
    )
}
