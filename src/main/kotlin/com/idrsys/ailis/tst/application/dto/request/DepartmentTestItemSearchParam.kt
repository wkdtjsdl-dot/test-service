package com.idrsys.ailis.tst.application.dto.request

data class DepartmentTestItemSearchParam(
    val deptCd: String,
    val tstLargeCateCd: String?,
    val tstMediumCateCd: String?,
    val tstCateCd: String?,
    val tstCateItemCd: String?,
)

data class UnspecifiedDepartmentTestItemSearchParam(
    val deptCd: String,
    val tstLargeCateCd: String?,
    val tstMediumCateCd: String?,
    val useYn: Boolean?
)
