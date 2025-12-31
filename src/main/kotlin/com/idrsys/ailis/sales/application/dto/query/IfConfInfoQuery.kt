package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class IfConfInfoQuery(
    val ifConfInfoId: String,
    val ifCustInfoId: String,
    val ifFieldInfoId: String,
    val ifFieldNm: String,
    val colIdx: Int,
    val creator: String,
    val createDtime: LocalDateTime,
)
