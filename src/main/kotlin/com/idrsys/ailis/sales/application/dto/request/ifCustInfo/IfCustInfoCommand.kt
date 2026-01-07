package com.idrsys.ailis.sales.application.dto.request.ifCustInfo

data class IfCustInfoCommand(
    val custMstId: String,
    val custCd: String,
    val headerInclYn: Boolean,
    val skipRowCnt: Int?,
    val ifDesc: String?,
    val confInfoList: List<IfConfInfoCommand> = emptyList(),
)

data class IfConfInfoCommand(
    val ifConfInfoId: String? = null,
    val ifFieldInfoId: String,
    val colIdx: Int,
)
