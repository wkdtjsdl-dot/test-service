package com.idrsys.ailis.tst.application.dto

import java.time.LocalDateTime

// Specimen Container DTOs
data class SpecimenContainerResponse(
    val spcmCntnCd: String,
    val cntnNm: String,
    val cntnEngNm: String,
    val cntnFileId: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class SpecimenContainerRegisterRequest(
    val spcmCntnCd: String,
    val cntnNm: String,
    val cntnEngNm: String,
    val cntnFileId: String
)

data class SpecimenContainerUpdateRequest(
    val cntnNm: String,
    val cntnEngNm: String,
    val cntnFileId: String
)

// Specimen DTOs
data class SpecimenResponse(
    val spcmCd: String,
    val spcmCateCd: String?,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String?,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class SpecimenRegisterRequest(
    val spcmCd: String,
    val spcmCateCd: String?,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String?,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String?,
)

data class SpecimenUpdateRequest(
    val spcmCateCd: String?,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String?,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String?,
)
