package com.idrsys.ailis.sales.application.dto.request.reqrstifmethod

import java.time.LocalDate

data class ReqRstIfMethodCommand(
    val reqRstIfMethodId: String? = null,
    val custMstId: String?,
    val applyStartDt: LocalDate? = null,
    val custCd: String,
    val applyEndDt: LocalDate? = null,
    val reqMethodCd: String,
    val reqIfTypeCd: String?,
    val useYn: Boolean = true
)
