package com.idrsys.ailis.sales.application.dto.request.exrt

import java.time.LocalDate

data class ExrtSearchParam(
    val stndDtFrom: LocalDate?,
    val stndDtTo: LocalDate?,
    val crcyCd: String?
)
