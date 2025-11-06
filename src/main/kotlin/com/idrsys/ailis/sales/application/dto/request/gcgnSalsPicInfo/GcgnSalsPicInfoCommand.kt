package com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo

import java.time.LocalDate

data class GcgnSalsPicInfoCommand(
    val custMstId: String,
    val applyStartDt: LocalDate,
    val salsTeamCd: String,
    val empno: String,
    val custCd: String,
    val applyEndDt: LocalDate?,
    val useYn: Boolean = true,
)
