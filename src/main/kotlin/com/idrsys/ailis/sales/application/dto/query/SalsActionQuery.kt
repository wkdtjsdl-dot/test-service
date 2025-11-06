package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class SalsActionQuery(
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
