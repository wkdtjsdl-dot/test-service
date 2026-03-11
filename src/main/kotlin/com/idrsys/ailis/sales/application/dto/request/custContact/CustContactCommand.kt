package com.idrsys.ailis.sales.application.dto.request.custContact

data class CustContactCommand(
    val custMstId: String,
    val custCd: String,
    val acctChargeNm: String?,
    val ofpoJbpo: String?,
    val telno: String?,
    val phno: String?,
    val email: String?,
    val remark: String?,
    val useYn: Boolean = true,
)
