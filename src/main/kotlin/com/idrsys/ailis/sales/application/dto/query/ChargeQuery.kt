package com.idrsys.ailis.sales.application.dto.query

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime

data class ChargeWithDetails (
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

    // Joined fields
    val custNm: String? = null,
    val bzoffiCd: String? = null,

    // 1:N 영업담당자 리스트
    @JsonProperty("sales_pics")
    val salesPics: List<SalesPicInfo> = emptyList()
)

data class SalesPicInfo(
    @JsonProperty("emp_user_id")
    val empUserId: String,
    @JsonProperty("cust_mst_id")
    val custMstId: String
)
