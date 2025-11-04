package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class HospitalMstResponse(
    val careInstId: String,
    val encpCareInstNo: String,
    val careInstNo: String?,
    val careInstNm: String,
    val asrtCd: String?,
    val estbDivNm: String?,
    val sidoCd: String?,
    val sidoNm: String?,
    val sgguCd: String?,
    val sgguNm: String?,
    val emd: String?,
    val zipcd: String?,
    val addr: String?,
    val telno: String?,
    val hpUrl: String?,
    val openDt: String?,
    val closeDt: String?,
    val drCnt: Int?,
    val sickbedCnt: Int?,
    val mapCodnX: Int?,
    val mapCodnY: Int?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class HospitalDeviceResponse(
    val hospDeviceId: Long,
    val careInstId: String?,
    val deviceCd: String,
    val deviceNm: String?,
    val deviceCnt: Int?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class HospitalMediSbjtResponse(
    val hospMediSbjtId: Long,
    val careInstId: String?,
    val mediSbjtCd: String,
    val mediSbjtNm: String?,
    val mediSbjtMdspCnt: Int?,
    val selcareDrCnt: Int?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)