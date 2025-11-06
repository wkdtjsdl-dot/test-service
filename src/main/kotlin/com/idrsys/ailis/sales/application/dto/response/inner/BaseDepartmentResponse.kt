package com.idrsys.ailis.sales.application.dto.response.inner

import java.time.LocalDate

data class BaseDepartmentResponse(
    val deptCd: String,
    val upDeptCd: String?,
    val deptNm: String,
    val deptAbbrNm: String?,
    val deptEngNm: String?,
    val deptEngAbbrNm: String?,
    val deptLvl: Int,
    val sortOrder: Int,
    val deptheadId: String?,
    val deptTypeCd: String,
    val locDivCd: String?,
    val deptOpenDt: LocalDate?,
    val deptCloseDt: LocalDate?,
    val telNo: String?,
    val faxNo: String?,
    val zipcd: String?,
    val addr1: String?,
    val addr2: String?,
    val bzoffiCd: String?,
    val useYn: Boolean
)
