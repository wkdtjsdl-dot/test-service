package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class CustAddInfoQuery(
    val custAddInfoId: Long,
    val custMstId: String,
    val custCd: String,
    val spnoteDivCd: String?,
    val spnote: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
