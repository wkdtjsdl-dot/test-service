package com.idrsys.ailis.sales.application.dto.request.chargeapprove

/**
 * 고객수가 승인요청 Command
 */
data class ChargeApproveRequestCommand(
    val custChargeId: String,  // 고객수가 ID
)

/**
 * 고객수가 승인/반려 Command
 */
data class ChargeApproveActionCommand(
    val custChargeId: String,  // 고객수가 ID
    val apprMemo: String? = null,  // 결재 메모
)
