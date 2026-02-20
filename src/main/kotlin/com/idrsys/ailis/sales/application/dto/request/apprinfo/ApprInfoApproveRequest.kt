package com.idrsys.ailis.sales.application.dto.request.apprinfo

data class ApprInfoApproveRequest(
    val apprInfoNo: Long,
    val apprSeq: Int,
    val apprMemo: String?,
    val userId: String
)
