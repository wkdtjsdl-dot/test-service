package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class GcgnSalsPicInfoResponse(
    val gcgnSalsPicInfoId: Long,
    val custMstId: String,
    val applyStartDt: LocalDate,
    val salsTeamCd: String,
    val empUserId: String,
    val custCd: String,
    val applyEndDt: LocalDate?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
    val empNm: String? = null,
)
