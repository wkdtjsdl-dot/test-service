package com.idrsys.ailis.tst.domain.command

import java.math.BigDecimal
import java.time.LocalDate

data class StandardChargeCreateCommand(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: BigDecimal?,
    val insurePrice: BigDecimal,
    val qladCharge: BigDecimal,
    val stndPrice: BigDecimal,
    val lowestCharge: BigDecimal,
    val qladCd: String?,
    val relatValueQladPoint: BigDecimal,
    val outputInsuCd: String?,
    val totalQladCharge: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal
)

data class StandardChargeUpdateCommand(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: BigDecimal?,
    val insurePrice: BigDecimal,
    val qladCharge: BigDecimal,
    val stndPrice: BigDecimal,
    val lowestCharge: BigDecimal,
    val qladCd: String?,
    val relatValueQladPoint: BigDecimal,
    val outputInsuCd: String?,
    val totalQladCharge: BigDecimal,
    val supval: BigDecimal,
    val addtax: BigDecimal
)

data class TestItemSpecimenCreateCommand(
    val tstCd: String,
    val spcmCd: String,
    val sortOrder: Int,
    val estlYn: Boolean,
    val takeQnty: String,
    val engTakeQnty: String,
    val useQnty: String,
    val engUseQnty: String,
    val strgMethod: String,
    val engStrgMethod: String,
    val spcmStbl: String,
    val engSpcmStbl: String,
    val takeMethod: String,
    val engTakeMethod: String,
    val spcmDesc: String,
    val engDesc: String,
    val caution: String,
    val engCaution: String,
    val spcmCntnCd: String
)
