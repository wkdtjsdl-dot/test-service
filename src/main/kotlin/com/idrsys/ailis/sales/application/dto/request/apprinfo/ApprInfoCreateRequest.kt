package com.idrsys.ailis.sales.application.dto.request.apprinfo

data class ApprInfoCreateRequest(
    val apprInfoNo: Long,
    val apprSeq: Int,
    val apprDocTypeCd: String,
    val apprPersonEmpNo: String,
    val apprStatCd: String,
    val apprMemo: String? = null,
    val creator: String
)
