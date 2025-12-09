package com.idrsys.ailis.tst.domain.command

import java.time.LocalDate

data class StandardChargeCreateCommand(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: Double?,
    val insuCharge: Double,
    val qladCharge: Double,
    val stndCharge: Double,
    val lowestCharge: Double,
    val qladCd: String?,
    val relatValueQladPoint: Double,
    val outputInsuCd: String?,
    val totalQladCharge: Double,
    val supval: Double,
    val addtax: Double
)

data class StandardChargeUpdateCommand(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: Double?,
    val insuCharge: Double,
    val qladCharge: Double,
    val stndCharge: Double,
    val lowestCharge: Double,
    val qladCd: String?,
    val relatValueQladPoint: Double,
    val outputInsuCd: String?,
    val totalQladCharge: Double,
    val supval: Double,
    val addtax: Double
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
    val strgMethodCd: String,
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
