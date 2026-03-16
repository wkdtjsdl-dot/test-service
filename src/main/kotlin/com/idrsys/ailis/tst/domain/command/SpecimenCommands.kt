package com.idrsys.ailis.tst.domain.command

data class SpecimenCreateCommand(
    val spcmCd: String,
    val spcmCateCd: String?,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String?,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String?,
)

data class SpecimenUpdateCommand(
    val spcmCateCd: String?,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String?,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String?,
)
