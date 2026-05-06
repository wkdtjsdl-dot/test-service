package com.idrsys.ailis.sales.application.dto.request.billing

import java.time.YearMonth

data class RecalculateBillingCommand(
    val yearMonth: YearMonth,
    val bzoffiCd: String? = null
)
