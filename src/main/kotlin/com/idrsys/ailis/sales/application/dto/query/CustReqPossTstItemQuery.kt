package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class CustReqPossTstItemQuery(
    val custReqPossTstItemId: Long,
    val custMstId: String?,
    val custCd: String,
    val tstCd: String,
    val tstNm: String?,
    val creator: String,
    val createDtime: LocalDateTime
)
