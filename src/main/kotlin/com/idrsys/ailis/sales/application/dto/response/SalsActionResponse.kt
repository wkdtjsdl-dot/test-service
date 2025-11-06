package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class SalsActionResponse(
    val salsActionId: Long,
    val custMstId: String,
    val custCd: String,
    val visitDtime: LocalDateTime?,
    val visitPrpsCd: String?,
    val visitTargetPersonNm: String?,
    val visitTargetPersonContact: String?,
    val memo: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
)
