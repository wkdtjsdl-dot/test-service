package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.time.LocalDate
import java.time.LocalDateTime

data class ChargeResponse(
    val custChargeId: String,
    val custMstId: String? = null,
    @ExcelColumn("고객코드")
    val custCd: String,
    @ExcelColumn("고객명")
    val custNm: String? = null,
    @ExcelColumn("영업소")
    val bzoffiNm: String? = null,
    @ExcelColumn("검사코드")
    val tstCd: String,
    @ExcelColumn("시작일자")
    val applyStartDt: LocalDate,
    @ExcelColumn("종료일자")
    val applyEndDt: LocalDate,
    @ExcelColumn("통화코드")
    val crcyCd: String,
    @ExcelColumn("공급가액")
    val supval: Long? = null,
    @ExcelColumn("부가세")
    val addtax: Long? = null,
    @ExcelColumn("특별수가")
    val specialCharge: Long,
    @ExcelColumn("등록일자")
    val createDtime: LocalDateTime,
    @ExcelColumn("등록자")
    val creator: String,
    @ExcelColumn("승인상태")
    val lastApprStatCd: String,
    val stndPrice: Long? = null,
    val remark: String? = null,
    val apprInfoId: String? = null,
    val currApprSeq: Int? = null,
    val apprSubmsEmpNo: String? = null,
    val apprSubmsDtime: LocalDateTime? = null,
    val apprLvlCd: String? = null,
    val updater: String,
    val updateDtime: LocalDateTime,
    val bzoffiCd: String? = null,
    val salesPics: List<SalesPicInfo>? = null
)

data class SalesPicInfo(
    val empUserId: String,
    val custMstId: String,
    val empUserNm: String? = null
)
