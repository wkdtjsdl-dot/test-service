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

data class TestItemAutoCompleteParam(
    val keyword: String
)

data class TestItemRegisterRequest(
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
    val tstIntNm: String?,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String?,
    val tstMethodCd: String?,
    val refVal: String?,
    val engRefVal: String?,
    val clncSgnf: String?,
    val engClncSgnf: String?,
    val tstDesc: String?,
    val tstEngDesc: String?,
    val tstDayweek: String?,
    val tstTatday: Int?,
    val insuApplyCd: String?,
    val insuCd: String?,
    val insuCateNo: String?
)

data class TestItemUpdateRequest(
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
    val tstIntNm: String?,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String?,
    val tstMethodCd: String?,
    val refVal: String?,
    val engRefVal: String?,
    val clncSgnf: String?,
    val engClncSgnf: String?,
    val tstDesc: String?,
    val tstEngDesc: String?,
    val tstDayweek: String?,
    val tstTatday: Int?,
    val insuApplyCd: String?,
    val insuCd: String?,
    val insuCateNo: String?,
    val updateReason: String?
)

data class TestItemResponse(
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
    val tstIntNm: String?,
    val rstTypeShortYn: Boolean,
    val rstTypeLongYn: Boolean,
    val rstTypeFileYn: Boolean,
    val rstTypeUrlYn: Boolean,
    val diseaseCd: String?,
    val tstMethodCd: String?,
    val refVal: String?,
    val engRefVal: String?,
    val clncSgnf: String?,
    val engClncSgnf: String?,
    val tstDesc: String?,
    val tstEngDesc: String?,
    val tstDayweek: String?,
    val tstTatday: Int?,
    val insuApplyCd: String?,
    val insuCd: String?,
    val insuCateNo: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class TestItemSimpleResponse(
    val tstCd: String,
    val tstNm: String
)

// --- Standard Charge ---

data class StandardChargeRegisterRequest(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: Double?,
    val insurePrice: Double,
    val qladCharge: Double,
    val stndPrice: Double,
    val lowestCharge: Double,
    val qladCd: String?,
    val relatValueQladPoint: Double,
    val outputInsuCd: String?,
    val totalQladCharge: Double,
    val supval: Double,
    val addtax: Double
)

data class StandardChargeUpdateRequest(
    val tstCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val insuCd: String?,
    val insuCateNo: String?,
    val relatValuePoint: Double?,
    val insurePrice: Double,
    val qladCharge: Double,
    val stndPrice: Double,
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
    val insurePrice: Double,
    val qladCharge: Double,
    val stndPrice: Double,
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
    val strgMethod: String,
    val engStrgMethod: String,
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

data class TestItemSpecimenUpdateRequest(
    val sortOrder: Int,
    val estlYn: Boolean,
    val takeQnty: String,
    val engTakeQnty: String,
    val useQnty: String,
    val engUseQnty: String,
    val strgMethod: String,
    val engStrgMethod: String,
    val spcmStbl: String?,
    val engSpcmStbl: String?,
    val takeMethod: String?,
    val engTakeMethod: String?,
    val spcmDesc: String,
    val engDesc: String?,
    val caution: String,
    val engCaution: String,
    val spcmCntnCd: String,
    val updateReason: String?
)

data class TestItemSpecimenDetailResponse(
    val spcmId: String,
    val tstCd: String,
    val spcmCd: String,
    val spcmNm: String?,
    val spcmCateCd: String?,
    val sortOrder: Int,
    val estlYn: Boolean,
    val takeQnty: String,
    val engTakeQnty: String,
    val useQnty: String,
    val engUseQnty: String,
    val strgMethod: String,
    val engStrgMethod: String,
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
    val updateDtime: LocalDateTime?
)

data class TestItemSpecimenListResponse(
    val spcmId: String,
    val tstCd: String,
    val spcmCd: String,
    val spcmNm: String?,
    val sortOrder: Int,
    val estlYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String?,
    val updateDtime: LocalDateTime?
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
    val updateDtime: LocalDateTime?
)

data class TestItemRefRequest(
    val refCateCd: String?,
    val tstCd: String
)

data class TestItemRefResponse(
    val refItemId: String,
    val tstCd: String,
    val refCd: String,
    val refCateCd: String?,
    val sortOrder: Int?,
    val refNm: String,
    val refEngNm: String?,
    val estlYn: Boolean
)

data class TestItemRefDetailResponse(
    val refItemId: String,
    val refCateCd: String,
    val tstCd: String,
    val refCd: String,
    val refNm: String,
    val refType: String,
    val refSize: Int?,
    val sortOrder: Int?,
    val estlYn: Boolean
)

// --- Gene ---

data class TestGeneRequest (
    val geneCd: String,
    val tstCd: String,
)
data class TestGeneResponse (
   val geneCd: String,
   val geneNm: String,
   val sortOrder: Int,
   val creator: String,
   val createDtime: LocalDateTime,
   val updater: String?,
   val updateDtime: LocalDateTime?,
        )

// --- Test Item Gene ---

data class TestItemGeneRegisterRequest(
    val tstCd: String,
    val geneCd: String
)

data class TestItemGeneResponse(
    val itemGeneId: String,
    val tstCd: String,
    val geneCd: String,
    val creator: String,
    val createDtime: LocalDateTime,
    val geneNm: String?,
)

// --- Test Item Essential Doc ---

data class TestItemEssentialDocRegisterRequest(
    val tstCd: String,
    val docCd: String
)

data class TestItemEssentialDocUpdateRequest(
    val docCd: String
)

data class TestItemEssentialDocResponse(
    val itemEstlDocId: String,
    val tstCd: String,
    val docCd: String,
    val creator: String,
    val createDtime: LocalDateTime,
)

data class TestItemEssentialDocListResponse(
    val itemEstlDocId: String,
    val tstCd: String,
    val docCd: String,
    val docDivCd: String,
    val docNm: String
)

data class TestItemEssentialDocDetailResponse(
    val itemEstlDocId: String,
    val tstCd: String,
    val docCd: String,
    val docDivCd: String,
    val docNm: String,
    val docEngNm: String,
    val docFileId: String,
    val docEngFileId: String
)

// --- Test Item Basic History ---

data class TestItemLogsSearchParam(
    val tstCd: String
)

data class TestItemLogsResponse(
    val itemHstId: String,
    val hstDesc: String,
    val tstCd: String,
    val tstNm: String,
    val editContents: String,
    val editBy: String,
    val editAt: LocalDateTime
)

// --- Test Item Specimen History ---

data class TestItemSpecimenLogsSearchParam(
    val tstCd: String,
    val spcmCd: String
)

data class TestItemSpecimenLogsResponse(
    val spcmHstId: String,
    val hstDesc: String,
    val tstCd: String,
    val spcmCd: String,
    val editContents: String,
    val editBy: String,
    val editAt: LocalDateTime
)

// --- Batch Specimens by Test Codes ---

data class SpecimenSimple(
    val spcmCd: String,
    val spcmNm: String?
)

data class TestItemSpecimensResponse(
    val tstCd: String,
    val specimens: List<SpecimenSimple>
)

// --- Batch Ref Items by Test Codes ---

data class RefItemSimple(
    val refCd: String,
    val refNm: String
)

data class RefItemDetail(
    val refCd: String,
    val refNm: String,
    val refEngNm: String?,
    val estlYn: Boolean,
    val dataFormat: String?
)

data class TestItemRefItemsResponse(
    val tstCd: String,
    val refItems: List<RefItemSimple>
)

data class TestItemRefDetailItemsResponse(
    val tstCd: String,
    val refItems: List<RefItemDetail>
)

// --- Test Item AutoComplete ---

/**
 * 검사종목 자동완성 응답 DTO
 */
data class TestItemAutoCompleteResponse(
    val tstCd: String,       // 검사코드
    val tstNm: String,       // 검사명
    val tstAbbrNm: String?   // 검사약어명
)
