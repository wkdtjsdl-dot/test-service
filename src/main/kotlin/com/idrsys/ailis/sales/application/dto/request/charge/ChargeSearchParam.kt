package com.idrsys.ailis.sales.application.dto.request.charge

import java.time.LocalDate

data class ChargeSearchParam(
    val bzoffiCd: String? = null,
    val custCd: String? = null,
    val custNm: String? = null,
    val tstCd: String? = null,
    val lastApprStatCd: String? = null,
    val includeHistory: Boolean? = false,
    val searchDate: LocalDate? = LocalDate.now(),
    val refDt: LocalDate? = null,
    val dateSearchType: String? = null,
    val startDt: LocalDate? = null,
    val endDt: LocalDate? = null,
    val custCdNm: String? = null,
    val empUserId: String? = null,
    val empUserNm: String? = null,
    val empUserIdNm: String? = null,
    val empUserIds: List<String> = emptyList(),
    val custMstId: String? = null
)
