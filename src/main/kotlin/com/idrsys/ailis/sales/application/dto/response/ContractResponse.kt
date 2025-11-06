package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.time.LocalDate
import java.time.LocalDateTime

data class ContractResponse(
    val custCntrId: Long,
    val custMstId: String,
    val custCd: String,
    val custNm: String?,
    val cntrNo: String?,
    val cntrDt: LocalDate?,
    val cntrStartDt: LocalDate?,
    val cntrEndDt: LocalDate?,
    val cntrType: String?,
    val recntrMonth: String?,
    val cntrNm: String?,
    val cntrCont: String?,
    val cntrPicId: String?,
    val cntrPicNm: String?,
    val atchGrupId: String,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
)


data class ContractListResponse(
    @ExcelColumn("계약번호")
    val cntrNo: String?,
    @ExcelColumn("계약시작일")
    val cntrStartDt: LocalDate?,
    @ExcelColumn("계약종료일")
    val cntrEndDt: LocalDate?,
    @ExcelColumn("계약일자")
    val cntrDt: LocalDate?,
    @ExcelColumn("계약명")
    val cntrNm: String?,
    @ExcelColumn("계약담당자")
    val cntrPicNm: String?,
    @ExcelColumn("등록자")
    val creator: String,
    @ExcelColumn("등록일")
    val createDtime: LocalDateTime,
    @ExcelColumn("수정자")
    val updater: String,
    @ExcelColumn("수정일")
    val updateDtime: LocalDateTime,
    val custCntrId: Long,
    val custMstId: String,
)
