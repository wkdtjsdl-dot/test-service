package com.idrsys.ailis.tst.domain.command

data class SpecimenCreateCommand(
    val spcmCd: String,
    val spcmCateCd: String,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String,
    val collAmt: String,
    val engCollAmt: String,
    val spcmStrg: String,
    val engSpcmStrg: String,
    val spcmSafe: String,
    val engSpcmSafe: String,
    val caution: String,
    val engCaution: String,
    val ref: String,
    val engRef: String
)

data class SpecimenUpdateCommand(
    val spcmCateCd: String,
    val useYn: Boolean,
    val spcmNm: String,
    val spcmAbbrNm: String,
    val spcmEngNm: String,
    val spcmEngAbbrNm: String,
    val collAmt: String,
    val engCollAmt: String,
    val spcmStrg: String,
    val engSpcmStrg: String,
    val spcmSafe: String,
    val engSpcmSafe: String,
    val caution: String,
    val engCaution: String,
    val ref: String,
    val engRef: String
)
