package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Billing Request Detail Response DTO
 *
 * Individual test item record for billing
 */
data class BillingRequestResponse(
    val tstItemId: String?,
    val tstReqDt: LocalDate,
    val tstReqNo: String,
    val tstReqDivCd: String,
    val custCd: String?,
    val custNm: String? = null,
    val patNm: String?,
    val hospChartNo: String?,
    val tstMediumCateCd: String?,
    val tstCd: String,
    val tstNm: String?,
    val stndPrice: BigDecimal,
    val supval: BigDecimal,
    val addTax: BigDecimal,
    val demandCharge: BigDecimal,
    val crcyCd: String?,
    val exrtPrice: BigDecimal?,
    val closingCd: String?,
    val closingSupval: BigDecimal?,
    val closingAddtax: BigDecimal?,
    val closingSpecialCharge: BigDecimal?,
    val discountRate: BigDecimal?,
    val creator: String,
    val createDtime: LocalDateTime,
    val closingMemo: String?
)

data class BillingRequestDomesticExcelRow(
    @ExcelColumn("의뢰일자") val tstReqDt: String?,
    @ExcelColumn("의뢰번호") val tstReqNo: String?,
    @ExcelColumn("의뢰구분") val tstReqDivCdNm: String?,
    @ExcelColumn("수진자명") val patNm: String?,
    @ExcelColumn("차트번호") val hospChartNo: String?,
    @ExcelColumn("재수탁거래처") val custNm: String?,
    @ExcelColumn("카테고리") val tstMediumCateNm: String?,
    @ExcelColumn("검사코드") val tstCd: String?,
    @ExcelColumn("검사항목명") val tstNm: String?,
    @ExcelColumn("기준수가") val stndPrice: BigDecimal?,
    @ExcelColumn("공급가액") val supval: BigDecimal?,
    @ExcelColumn("부가세액") val addTax: BigDecimal?,
    @ExcelColumn("청구수가") val demandCharge: BigDecimal?,
    @ExcelColumn("할인율") val discountRate: BigDecimal?,
    @ExcelColumn("등록자ID") val creator: String?,
    @ExcelColumn("등록일시") val createDtime: String?,
    @ExcelColumn("메모") val closingMemo: String?,
)

data class BillingRequestForeignExcelRow(
    @ExcelColumn("의뢰일자") val tstReqDt: String?,
    @ExcelColumn("의뢰번호") val tstReqNo: String?,
    @ExcelColumn("수진자명") val patNm: String?,
    @ExcelColumn("차트번호") val hospChartNo: String?,
    @ExcelColumn("카테고리") val tstMediumCateNm: String?,
    @ExcelColumn("검사코드") val tstCd: String?,
    @ExcelColumn("검사항목명") val tstNm: String?,
    @ExcelColumn("통화코드") val crcyCdNm: String?,
    @ExcelColumn("계약단가") val demandCharge: BigDecimal?,
    @ExcelColumn("공급가액") val supval: BigDecimal?,
    @ExcelColumn("적용환율") val exrtPrice: BigDecimal?,
    @ExcelColumn("마감 계약단가(원화)") val closingSpecialCharge: BigDecimal?,
    @ExcelColumn("마감 공급가(원화)") val closingSupval: BigDecimal?,
    @ExcelColumn("등록자ID") val creator: String?,
    @ExcelColumn("등록일시") val createDtime: String?,
    @ExcelColumn("메모") val closingMemo: String?,
)

internal fun BillingRequestResponse.toDomesticExcelRow(
    tstReqDivCdNm: String?,
    tstMediumCateNm: String?,
) = BillingRequestDomesticExcelRow(
    tstReqDt = tstReqDt?.toString(),
    tstReqNo = tstReqNo,
    tstReqDivCdNm = tstReqDivCdNm,
    patNm = patNm,
    hospChartNo = hospChartNo,
    custNm = custNm,
    tstMediumCateNm = tstMediumCateNm,
    tstCd = tstCd,
    tstNm = tstNm,
    stndPrice = stndPrice,
    supval = if (closingCd == "CLCD_Y") closingSupval ?: supval else supval,
    addTax = if (closingCd == "CLCD_Y") closingAddtax ?: addTax else addTax,
    demandCharge = if (closingCd == "CLCD_Y") closingSpecialCharge ?: demandCharge else demandCharge,
    discountRate = discountRate,
    creator = creator,
    createDtime = createDtime.toString().replace("T", " ").substringBefore("."),
    closingMemo = closingMemo,
)

internal fun BillingRequestResponse.toForeignExcelRow(
    crcyCdNm: String?,
    tstMediumCateNm: String?,
) = BillingRequestForeignExcelRow(
    tstReqDt = tstReqDt?.toString(),
    tstReqNo = tstReqNo,
    patNm = patNm,
    hospChartNo = hospChartNo,
    tstMediumCateNm = tstMediumCateNm,
    tstCd = tstCd,
    tstNm = tstNm,
    crcyCdNm = crcyCdNm,
    demandCharge = demandCharge,
    supval = supval,
    exrtPrice = exrtPrice,
    closingSpecialCharge = closingSpecialCharge,
    closingSupval = closingSupval,
    creator = creator,
    createDtime = createDtime.toString().replace("T", " ").substringBefore("."),
    closingMemo = closingMemo,
)
