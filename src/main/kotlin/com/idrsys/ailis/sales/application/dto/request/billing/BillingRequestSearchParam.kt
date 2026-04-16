package com.idrsys.ailis.sales.application.dto.request.billing

import io.swagger.v3.oas.annotations.Parameter
import java.time.LocalDate

/**
 * Billing Request Search Parameters
 *
 * Parameters for querying billing request details
 */
data class BillingRequestSearchParam(
    @Parameter(description = "고객코드 (= 직접거래처코드)", required = true)
    val custCd: String,

    @Parameter(description = "의뢰 시작일", required = true)
    val startDt: LocalDate,

    @Parameter(description = "의뢰 종료일", required = true)
    val endDt: LocalDate,

    @Parameter(description = "청구 ID (특정 청구 건의 의뢰내역 조회 시)", required = false)
    val demandId: String? = null,

    @Parameter(description = "마감코드 (CLCD_Y: 마감, CLCD_N: 미마감)", required = false)
    val closingCd: String? = null,

    @Parameter(description = "청구구분 (10: 일반, 30: 선수금)", required = false)
    val demandType: String? = null
)
