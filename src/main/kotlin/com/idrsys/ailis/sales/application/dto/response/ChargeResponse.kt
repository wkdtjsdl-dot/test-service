package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class ChargeResponse(
    val custChargeId: String,
    val custMstId: String? = null,
    val custCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val tstCd: String,
    val crcyCd: String,
    val stndPrice: Long? = null,
    val specialCharge: Long,
    val supval: Long? = null,
    val addtax: Long? = null,
    val remark: String? = null,
    val apprInfoId: String? = null,
    val currApprSeq: Int? = null,
    val apprSubmsEmpNo: String? = null,
    val apprSubmsDtime: LocalDateTime? = null,
    val lastApprStatCd: String,
    val apprLvlCd: String? = null,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,

    val custNm: String? = null,
    val bzoffiCd: String? = null,
    val bzoffiNm: String? = null,

    val salesPics: List<SalesPicInfo>? = null
)

data class SalesPicInfo(
    val empUserId: String,
    val custMstId: String,
    val empUserNm: String? = null
)
