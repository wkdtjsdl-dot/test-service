package com.idrsys.ailis.sales.application.dto.request.hploginuser

import java.time.LocalDateTime

data class HpLoginUserCommand(
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
    val useYn: Boolean = true
)
