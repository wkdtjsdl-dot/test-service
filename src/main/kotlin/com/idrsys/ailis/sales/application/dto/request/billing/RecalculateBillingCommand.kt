package com.idrsys.ailis.sales.application.dto.request.billing

import java.time.LocalDate

data class RecalculateBillingCommand(
    val startDt: LocalDate,
    val endDt: LocalDate,
    val bzoffiCd: String? = null
)
