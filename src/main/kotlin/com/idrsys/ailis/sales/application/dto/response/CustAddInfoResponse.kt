package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class CustAddInfoResponse(
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
