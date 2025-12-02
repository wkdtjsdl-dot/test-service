package com.idrsys.ailis.tst.application.dto

import java.time.LocalDate
import java.time.LocalDateTime

// --- Test Item ---

data class TestItemSearchParam(
    val deptCd: String?,
    val tstLargeCateCd: String?,
    val tstMediumCateCd: String?,
    val useYn: Boolean?
)

data class TestItemRegisterRequest(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val reqPossYn: Boolean,
    val webYn: Boolean,
    val tstNm: String,
    val tstAbbrNm: String,
    val tstEngNm: String,
    val tstEngAbbrNm: String,
    val tstIntNm: String,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String,
    val tstMethodCd: String?,
    val refVal: String,
    val engRefVal: String,
    val clncSgnf: String,
    val engClncSgnf: String,
    val tstDesc: String,
    val tstEngDesc: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val insuApplyCd: String,
    val insuCd: String,
    val insuCateNo: String
)

data class TestItemUpdateRequest(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val reqPossYn: Boolean,
    val webYn: Boolean,
    val tstNm: String,
    val tstAbbrNm: String,
    val tstEngNm: String,
    val tstEngAbbrNm: String,
    val tstIntNm: String,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String,
    val tstMethodCd: String?,
    val refVal: String,
    val engRefVal: String,
    val clncSgnf: String,
    val engClncSgnf: String,
    val tstDesc: String,
    val tstEngDesc: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val insuApplyCd: String,
    val insuCd: String,
    val insuCateNo: String
)

data class TestItemResponse(
    val tstCd: String,
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val reqPossYn: Boolean,
    val webYn: Boolean,
    val tstNm: String,
    val tstAbbrNm: String,
    val tstEngNm: String,
    val tstEngAbbrNm: String,
    val tstIntNm: String,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String,
    val tstMethodCd: String?,
    val refVal: String,
    val engRefVal: String,
    val clncSgnf: String,
    val engClncSgnf: String,
    val tstDesc: String,
    val tstEngDesc: String,
    val tstDayweek: String,
    val tstTatday: Int,
    val insuApplyCd: String,
    val insuCd: String,
    val insuCateNo: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String?,
    val updateDetime: LocalDateTime?
)

// --- Standard Charge ---

data class StandardChargeRegisterRequest(
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

data class StandardChargeResponse(
    val stndChargeId: String,
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
    val addtax: Double,
    val creator: String,
    val createDtime: LocalDateTime
)

// --- Test Item Specimen ---

data class TestItemSpecimenRegisterRequest(
    val tstCd: String,
    val spcmCd: String,
    val sortOrder: Int,
    val estlYn: Boolean,
    val takeQnty: String,
    val engTakeQnty: String,
    val useQnty: String,
    val engUseQnty: String,
    val strgMethodCd: String,
    val spcmStbl: String?,
    val engSpcmStbl: String?,
    val takeMethod: String?,
    val engTakeMethod: String?,
    val spcmDesc: String,
    val engDesc: String?,
    val caution: String,
    val engCaution: String,
    val spcmCntnCd: String
)

data class TestItemSpecimenResponse(
    val spcmId: String,
    val tstCd: String,
    val spcmCd: String,
    val sortOrder: Int,
    val estlYn: Boolean,
    val takeQnty: String,
    val engTakeQnty: String,
    val useQnty: String,
    val engUseQnty: String,
    val strgMethodCd: String,
    val spcmStbl: String?,
    val engSpcmStbl: String?,
    val takeMethod: String?,
    val engTakeMethod: String?,
    val spcmDesc: String,
    val engDesc: String?,
    val caution: String,
    val engCaution: String,
    val spcmCntnCd: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String?,
    val updateDetime: LocalDateTime?
)

// --- Test Item Ref Item ---

data class TestItemRefItemRegisterRequest(
    val tstCd: String,
    val refCd: String,
    val estlYn: Boolean,
    val sortOrder: Int
)

data class TestItemRefItemUpdateRequest(
    val estlYn: Boolean,
    val sortOrder: Int
)

data class TestItemRefItemResponse(
    val refItemId: String,
    val tstCd: String,
    val refCd: String,
    val estlYn: Boolean,
    val sortOrder: Int,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String?,
    val updateDetime: LocalDateTime?
)
