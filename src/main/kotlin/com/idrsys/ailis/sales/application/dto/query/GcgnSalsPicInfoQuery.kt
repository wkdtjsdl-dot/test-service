package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDate
import java.time.LocalDateTime

data class GcgnSalsPicInfoQuery(
    val gcgnSalsPicInfoId: Long,
    val custMstId: String,
    val applyStartDt: LocalDate,
    val salsTeamCd: String,
    val empno: String,
    val custCd: String,
    val applyEndDt: LocalDate?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
    val empNm: String? = null,
)
