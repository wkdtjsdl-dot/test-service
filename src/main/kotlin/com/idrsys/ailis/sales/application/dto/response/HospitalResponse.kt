package com.idrsys.ailis.sales.application.dto.response

data class HospitalMstResponse(
    val careInstId: String,
    val careInstNm: String,
    val addr: String?,
    val telno: String?,
    val zipcd: String?,
    val asrtCd: String?,
)
