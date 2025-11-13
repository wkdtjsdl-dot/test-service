package com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo

data class GcgnSalsPicInfoSearchParam(
    val custMstId: String?,
    val salsTeamCd: String?,
    val empUserId: String?,
    val empNm: String?
)
data class GcgnSalaPicInfoAutoSearchParam(
    val empUserIdNm: String?
)
