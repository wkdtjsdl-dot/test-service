package com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo

data class GcgnSalsPicInfoSearchParam(
    val custMstId: String?,
    val salsTeamCd: String?,
    val empUserId: String?,
    val empNm: String?,
    val empUserIds: List<String?> = emptyList(), // inner 호출용 추가
    val custCd: String?     // inner 호출용 추가
)
data class GcgnSalaPicInfoAutoSearchParam(
    val empUserIdNm: String?
)
