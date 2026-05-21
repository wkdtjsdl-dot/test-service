package com.idrsys.ailis.sales.application.dto.request.chargeapprove

import java.time.LocalDate

data class ChargeApproveSearchParam(
    val custCd: String? = null,
    val custNm: String? = null,
    val custCdNm: String? = null,
    val tstCd: String? = null,
    val applyStartDt: LocalDate? = null,
    val applyEndDt: LocalDate? = null,
    val dateSearchType: String? = null,
    val startDt: LocalDate? = null,
    val endDt: LocalDate? = null,
    val lastApprStatCd: String? = null,
    val apprLvlCd: String? = null,
    val myApproval: Boolean = false,
    val bzoffiCd: String? = null,
)
