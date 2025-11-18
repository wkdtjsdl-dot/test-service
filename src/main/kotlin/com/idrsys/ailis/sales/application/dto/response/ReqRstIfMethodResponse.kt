package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class ReqRstIfMethodResponse(
    val reqRstIfMethodId: String,
    val custMstId: String?,
    val applyStartDt: LocalDate,
    val custCd: String,
    val applyEndDt: LocalDate?,
    val reqMethodCd: String,
    val reqIfTypeCd: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
