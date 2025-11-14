package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class CustReqPossTstItemResponse(
    val custReqPossTstItemId: Long,
    val custMstId: String?,
    val custCd: String,
    val tstCd: String,
    val creator: String,
    val createDtime: LocalDateTime
)
