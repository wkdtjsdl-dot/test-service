package com.idrsys.ailis.sales.application.dto.response.inner

import java.time.LocalDate
import java.time.LocalDateTime

data class BaseUserResponse(
    val userId: String,
    val userNm: String,
    val deptCd: String?,
    val dept: BaseDepartmentDetailResponse?,
    val empNo: String?,
    val langTypeCd: String?,
    val natnCd: String?,
    val userEngNm: String?,
    val ofpoCd: String?,
    val jbpoCd: String?,
    val emailAddr: String?,
    val bzMoblPhno: String?,
    val moblPhno: String?,
    val offcTelNo: String?,
    val emplDivCd: String,
    val userStat: String,
    val lastLoginDtime: LocalDateTime?,
    val ecnyDt: LocalDate,
    val lcnyDt: LocalDate?,
    val basicRoleCd: String,
    val addRoleLst: String?,
    val remark: String?,
    val loginPossYn: Boolean,
    val useYn: Boolean
)
