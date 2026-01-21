package com.idrsys.ailis.sales.application.dto.request.chargeapprove

import jakarta.validation.constraints.NotBlank

data class ChargeApproveActionCommand(
    @field:NotBlank
    val custChargeId: String,
    val apprMemo: String? = null
)
