package com.idrsys.ailis.sales.application.dto.response

import java.time.LocalDateTime

data class TestCodeMappingResponse (
    val custTstCdMpgId: String,
    val custMstId: String?,
    val custCd: String,
    val custNm: String?,
    val custTstCd: String,
    val custSubTstCd: String?,
    val custTstNm: String?,
    val tstCd: String?,
    val tstNm: String?,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,
)

data class TestCodeMappingExcelValidResponse (
    val custCd: String,
    val tstCd: String,
    val validCustCd: Boolean,
    val validTstCd: Boolean
)