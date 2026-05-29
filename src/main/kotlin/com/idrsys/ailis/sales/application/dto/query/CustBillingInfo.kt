package com.idrsys.ailis.sales.application.dto.query

/**
 * Customer Billing Info DTO
 * Used for batch query to get customer info for billing
 */
data class CustBillingInfo(
    val custNm: String,
    val billPublYn: Boolean,
    val invcRecpEmailYn: Boolean,
    val invcRecpEmailAddr: String,
    val bzoffiCd: String?,
    val sapCustCd: String?,
    val crcyCd: String?
)

data class RprsBillingInfo(
    val rprsCustCd: String,
    val rprsAcctBillCombPublYn: Boolean
)
