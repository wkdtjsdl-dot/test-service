package com.idrsys.ailis.sales.application.dto.request.custatchfile

data class CustAtchFileCommand(
    val custAtchFileId: String,
    val custMstId: String,
    val custCd: String,
    val atchGrupId: String,
    val useYn: Boolean = true
)
