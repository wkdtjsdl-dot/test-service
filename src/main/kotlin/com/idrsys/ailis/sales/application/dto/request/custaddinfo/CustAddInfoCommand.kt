package com.idrsys.ailis.sales.application.dto.request.custaddinfo

data class CustAddInfoCommand(
    val custMstId: String,
    val custCd: String,
    val spnoteDivCd: String?,
    val spnote: String?,
    val useYn: Boolean = true
)
