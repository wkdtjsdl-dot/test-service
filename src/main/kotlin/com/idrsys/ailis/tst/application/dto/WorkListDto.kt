package com.idrsys.ailis.tst.application.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class WorkListRegisterRequest(
    val wrklistCd: String,
    val useYn: Boolean,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val wrklistNm: String?
)

data class WorkListUpdateRequest(
    val useYn: Boolean,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val wrklistNm: String?
)

data class WorkListResponse(
    val wrklistCd: String,
    val useYn: Boolean,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val wrklistNm: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class WorkListItemRegisterRequest(
    val tstCd: String,
    val spcmCd: String?,
    val tstOption: String?,
    val wrklistCd: String
)

data class WorkListItemUpdateRequest(
    val tstCd: String,
    val spcmCd: String?,
    val tstOption: String?
)

data class WorkListItemResponse(
    val wrklistItmId: String,
    val wrklistCd: String,
    val tstCd: String,
    val spcmCd: String?,
    val tstOption: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class WorkListItemDetailResponse(
    val wrklistItmId: String,
    val wrklistCd: String,
    val tstCd: String,
    val tstNm: String?,
    val spcmCd: String?,
    val spcmNm: String?,
    val tstOption: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
