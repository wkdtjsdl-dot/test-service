package com.idrsys.ailis.sales.application.dto.response.inner

/**
 * TstItem closing response from req-service (Inner API)
 */
data class ReqServiceClosingResponse(
    val updatedCount: Int,
    val directAcctCd: String
)

/**
 * TstItem closing release response from req-service (Inner API)
 */
data class ReqServiceClosingReleaseResponse(
    val releasedCount: Int,
    val directAcctCd: String
)
