package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class ExrtResponse(
    val exrtId: Long?,
    val stndDt: LocalDate,
    val crcyCd: String,
    val stndExrt: BigDecimal,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)

data class ExrtListResponse(
    @ExcelColumn("기준일자")
    val stndDt: LocalDate,
    @ExcelColumn("통화코드")
    val crcyCd: String,
    @ExcelColumn("기준환율")
    val stndExrt: BigDecimal,
    @ExcelColumn("등록자")
    val creator: String,
    @ExcelColumn("등록일시")
    val createDtime: LocalDateTime,
    @ExcelColumn("수정자")
    val updater: String,
    @ExcelColumn("수정일시")
    val updateDtime: LocalDateTime,
    val exrtId: Long?
)

data class ExrtBatchResponse(
    val totalCount: Int,
    val insertedCount: Int,
    val skippedCount: Int
)
