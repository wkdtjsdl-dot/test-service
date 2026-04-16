package com.idrsys.ailis.tst.domain.command

import java.time.LocalDate

data class TestItemSubCreateCommand(
    val tstCd: String,
    val tstSubCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val tstSubNm: String,
    val tstSubAbbrNm: String,
    val tstSubEngNm: String,
    val tstSubEngAbbrNm: String,
    val tstSubIntNm: String?,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val refVal: String?,
    val engRefVal: String?
)

data class TestItemSubUpdateCommand(
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val tstSubNm: String,
    val tstSubAbbrNm: String,
    val tstSubEngNm: String,
    val tstSubEngAbbrNm: String,
    val tstSubIntNm: String?,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val refVal: String?,
    val engRefVal: String?
)
