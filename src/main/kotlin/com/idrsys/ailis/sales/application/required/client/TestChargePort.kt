package com.idrsys.ailis.sales.application.required.client

/**
 * test-service 검사 기준수가 조회 Client (Port)
 */
interface TestChargePort {
    /**
     * 검사 기준수가 조회
     * @param tstCd 검사코드
     * @return 기준수가 목록 (lowestCharge 포함)
     */
    suspend fun getStandardCharges(tstCd: String): List<StandardChargeResponse>
}

/**
 * test-service StandardChargeResponse DTO
 */
data class StandardChargeResponse(
    val stndChargeId: String,
    val tstCd: String,
    val lowestCharge: Double,  // 최저수가
    val stndCharge: Double,    // 기준수가
)
