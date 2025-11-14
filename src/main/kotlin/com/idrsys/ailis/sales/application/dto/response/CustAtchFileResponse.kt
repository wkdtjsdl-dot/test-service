package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class CustAtchFileResponse(
    val custAtchFileId: String,
    val custMstId: String,
    val custCd: String,
    val atchGrupId: String,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
