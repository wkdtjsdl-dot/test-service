package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class CustLogsSearchParam (
    val custMstId: String,
)

data class CustLogsEditResponse (
    val custMstHstId: Long,
    val custMstId: String,
    val custCd: String,
    val custNm: String,
    val editBy: String,
    val editAt: LocalDateTime,
    val updateReason: String?,
    val editContents: String
)

data class CustDetailLogs (
    val custMstId: String,
    val fieldName: String?,
    val oldValue: String?,
    val newValue: String?,
)
