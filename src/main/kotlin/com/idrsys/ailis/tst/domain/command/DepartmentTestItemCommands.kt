package com.idrsys.ailis.tst.domain.command

data class DepartmentTestItemCreateCommand(
    val deptCd: String,
    val tstCd: String,
    val tstNm: String,
    val tstAbbrNm: String,
    val tstEngNm: String,
    val tstEngAbbrNm: String,
    val sortOrder: Int,
    val useYn: Boolean
)

data class DepartmentTestItemUpdateCommand(
    val deptCd: String,
    val tstCd: String,
    val tstNm: String,
    val tstAbbrNm: String,
    val tstEngNm: String,
    val tstEngAbbrNm: String,
    val sortOrder: Int,
    val useYn: Boolean
)
