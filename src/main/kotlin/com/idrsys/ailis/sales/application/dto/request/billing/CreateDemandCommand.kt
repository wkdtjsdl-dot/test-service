package com.idrsys.ailis.sales.application.dto.request.billing

import java.time.LocalDate

/**
 * Create Demand Command
 *
 * Command to create a billing demand (청구서 생성 마감)
 */
data class CreateDemandCommand(
    val custCd: String,
    val demandDt: LocalDate,
    val demandStartDt: LocalDate,
    val demandStndDt: LocalDate,
    val exrtId: Long? = null,
    val demandMemo: String? = null
)
