package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDateTime

data class HpLoginUserQuery(
    val hpLoginUserId: String,
    val custMstId: String,
    val custCd: String,
    val hpCustDiv: String?,
    val loginId: String,
    val loginPswd: String,
    val loginFailNum: Int?,
    val pswdChngDtime: LocalDateTime?,
    val lastLoginDtime: LocalDateTime?,
    val loginNm: String?,
    val loginPersonContact: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
