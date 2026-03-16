package com.idrsys.ailis.tst.domain.command

data class DepartmentGroupCreateCommand(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateNm: String,
    val updateAuthCd: String,
    val dupAllowYn: Boolean,
    val sortOrder: Int
)

data class DepartmentGroupUpdateCommand(
    val deptCd: String,
    val tstCateCd: String,
    val tstCateNm: String,
    val updateAuthCd: String,
    val dupAllowYn: Boolean,
    val sortOrder: Int
)
