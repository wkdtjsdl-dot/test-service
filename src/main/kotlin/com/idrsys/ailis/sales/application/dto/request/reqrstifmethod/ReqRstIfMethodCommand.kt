package com.idrsys.ailis.sales.application.dto.request.reqrstifmethod

import java.time.LocalDate

data class ReqRstIfMethodCommand(
    val reqRstIfMethodId: String,
    val custMstId: String?,
    val applyStartDt: LocalDate,
    val custCd: String,
    val applyEndDt: LocalDate?,
    val reqMethodCd: String,
    val reqIfTypeCd: String?,
    val useYn: Boolean = true
)
