package com.idrsys.ailis.sales.application.dto.request.testCodeMapping

import java.util.*

data class TestCodeMappingCommand(
    var custTstCdMpgId: UUID?,
    var custMstId: String?,
    val custCd: String,
    val custTstCd: String,
    val custSubTstCd: String?,
    val custTstNm: String?,
    val tstCd: String?,
    val tstNm: String?,
)
