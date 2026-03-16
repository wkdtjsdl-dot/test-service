package com.idrsys.ailis.tst.domain.command

data class DepartmentTestItemCreateCommand(
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val deptTstDesc: String?
)

data class DepartmentTestItemUpdateCommand(
    val deptCd: String,
    val tstCd: String,
    val danDivCd: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val deptTstDesc: String?
)
