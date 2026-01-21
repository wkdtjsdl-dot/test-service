package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class IfCustInfoQuery(
    val ifCustInfoId: String,
    val custMstId: String,
    val custCd: String,
    val custNm: String?,
    val headerInclYn: Boolean,
    val skipRowCnt: Int?,
    val ifDesc: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
)
