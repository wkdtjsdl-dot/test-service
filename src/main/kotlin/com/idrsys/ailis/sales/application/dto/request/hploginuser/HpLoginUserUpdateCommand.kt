package com.idrsys.ailis.sales.application.dto.request.hploginuser

data class HpLoginUserUpdateCommand(
    val hpLoginUserId: String,
    val custMstId: String,
    val custCd: String,
    val hpCustDiv: String?,
    val loginId: String,
    val loginPswd: String?,   // nullable: 입력 시에만 변경
    val loginNm: String?,
    val loginPersonContact: String?,
    val useYn: Boolean = true
)
