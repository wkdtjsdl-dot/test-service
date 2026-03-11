package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class IfCustInfoResponse(
    val ifCustInfoId: String,
    val custMstId: String,
    val custCd: String,
    val custNm: String?,
    val headerInclYn: Boolean,
    val skipRowCnt: Int?,
    val ifDesc: String?,
    val confInfoList: List<IfConfInfoResponse> = emptyList(),
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
)

data class IfConfInfoResponse(
    val ifConfInfoId: String,
    val ifFieldInfoId: String,
    val ifFieldNm: String,
    val colIdx: Int,
    val creator: String,
    val createDtime: LocalDateTime,
)
