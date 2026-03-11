package com.idrsys.ailis.sales.application.dto.request.salsAction

import java.time.LocalDateTime

data class SalsActionCommand(
    val custMstId: String,
    val custCd: String,
    val visitDtime: LocalDateTime?,
    val visitPrpsCd: String?,
    val visitTargetPersonNm: String?,
    val visitTargetPersonContact: String?,
    val memo: String?,
    val useYn: Boolean = true,
)
