package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 고객수가 승인 조회용 Query DTO
 */
data class ChargeApproveQuery(
    val custChargeId: String,
    val custCd: String,
    val custNm: String?,
    val tstCd: String,
    val tstNm: String?,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val specialCharge: Long,
    val stndPrice: Long?,
    val supval: Long?,
    val addtax: Long?,
    val remark: String?,
    val apprInfoNo: Long?,
    val currApprSeq: Int?,
    val apprSubmsEmpNo: String?,
    val apprSubmsDtime: LocalDateTime?,
    val lastApprStatCd: String,
    val apprLvlCd: String?,
)
