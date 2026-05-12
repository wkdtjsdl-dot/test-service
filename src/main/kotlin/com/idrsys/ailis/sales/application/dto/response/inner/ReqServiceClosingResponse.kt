package com.idrsys.ailis.sales.application.dto.response.inner

/**
 * TstItem closing response from req-service (Inner API)
 */
data class ReqServiceClosingResponse(
    val updatedCount: Int
)

/**
 * TstItem closing release response from req-service (Inner API)
 */
data class ReqServiceClosingReleaseResponse(
    val releasedCount: Int
)
