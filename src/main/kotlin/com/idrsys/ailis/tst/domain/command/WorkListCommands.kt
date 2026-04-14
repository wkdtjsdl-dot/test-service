package com.idrsys.ailis.tst.domain.command

import java.time.LocalDate

data class WorkListCreateCommand(
    val wrklistCd: String,
    val useYn: Boolean,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val wrklistNm: String?
)

data class WorkListUpdateCommand(
    val useYn: Boolean,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val wrklistNm: String?
)

data class WorkListItemCreateCommand(
    val tstCd: String,
    val spcmCd: String?,
    val tstOption: String?,
    val wrklistCd: String
)

data class WorkListItemUpdateCommand(
    val tstCd: String,
    val spcmCd: String?,
    val tstOption: String?
)
