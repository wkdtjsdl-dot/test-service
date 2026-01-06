package com.idrsys.ailis.sales.application.dto.request.estimate

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Create Estimate Command
 */
data class CreateEstimateCommand(
    val docType: String,  // "EST": 견적서, "TRN": 거래명세서
    val regDt: LocalDate,
    val title: String,
    val receiver: String? = null,
    val reference: String? = null,
    val writerEmpNo: String? = null,
    val deptCd: String? = null,
    val remark: String? = null,
    val note: String? = null,
    val items: List<EstimateItemCommand>
)

/**
 * Update Estimate Command
 */
data class UpdateEstimateCommand(
    val title: String,
    val receiver: String? = null,
    val reference: String? = null,
    val writerEmpNo: String? = null,
    val deptCd: String? = null,
    val remark: String? = null,
    val note: String? = null,
    val items: List<EstimateItemCommand>
)

/**
 * Estimate Item Command
 */
data class EstimateItemCommand(
    val itemNm: String,
    val qty: BigDecimal,
    val unitPrice: BigDecimal
)
