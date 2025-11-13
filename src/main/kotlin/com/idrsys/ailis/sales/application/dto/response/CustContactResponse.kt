package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class CustContactResponse(
    val custContactId: Long,
    val custMstId: String,
    val custCd: String,
    val acctChargeNm: String?,
    val ofpoJbpo: String?,
    val telno: String?,
    val phno: String?,
    val email: String?,
    val remark: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
    val empNm: String? = null,
)
