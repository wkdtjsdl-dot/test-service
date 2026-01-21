package com.idrsys.ailis.sales.application.dto.request.chargeapprove

import jakarta.validation.constraints.NotBlank

data class ChargeApproveRequestCommand(
    @field:NotBlank
    val custChargeId: String
)
