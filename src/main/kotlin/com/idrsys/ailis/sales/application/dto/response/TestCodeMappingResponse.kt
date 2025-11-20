package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.web.excel.ExcelColumn
import java.time.LocalDateTime

data class TestCodeMappingResponse (
    val custTstCdMpgId: String,
    val custMstId: String?,
    @ExcelColumn("고객코드")
    val custCd: String,
    @ExcelColumn("고객명")
    val custNm: String?,
    @ExcelColumn("고객검사코드")
    val custTstCd: String,
    @ExcelColumn("고객부속코드")
    val custSubTstCd: String?,
    @ExcelColumn("고객검사항목명")
    val custTstNm: String?,
    @ExcelColumn("지놈검사코드")
    val tstCd: String?,
    @ExcelColumn("지놈검사항목명")
    val tstNm: String?,
    @ExcelColumn("등록자")
    val creator: String,
    @ExcelColumn("등록일시")
    val createDtime: LocalDateTime,
    @ExcelColumn("수정자")
    val updater: String,
    @ExcelColumn("수정일시")
    val updateDtime: LocalDateTime,
)

data class TestCodeMappingExcelValidResponse (
    val custCd: String,
    val tstCd: String,
    val validCustCd: Boolean,
    val validTstCd: Boolean
)