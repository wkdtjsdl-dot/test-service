package com.idrsys.ailis.sales.application.dto.request.exrt

import java.math.BigDecimal
import java.time.LocalDate

data class ExrtBatchCommand(
    val stndDt: LocalDate,
    val crcyCd: String,
    val stndExrt: BigDecimal
)
