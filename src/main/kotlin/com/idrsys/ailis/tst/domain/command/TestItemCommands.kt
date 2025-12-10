package com.idrsys.ailis.tst.domain.command

import java.time.LocalDate

data class TestItemCreateCommand(
    val tstCd: String,
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val reqPossYn: Boolean,
    val webKorYn: Boolean,
    val webEngYn: Boolean,
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
    val tstMethodCd: String,
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

data class TestItemUpdateCommand(
    val tstLargeCateCd: String,
    val tstMediumCateCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate,
    val useYn: Boolean,
    val reqPossYn: Boolean,
    val webKorYn: Boolean,
    val webEngYn: Boolean,
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
    val tstMethodCd: String,
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

data class TestItemGeneCreateCommand(
    val tstCd: String,
    val geneCd: String
)
