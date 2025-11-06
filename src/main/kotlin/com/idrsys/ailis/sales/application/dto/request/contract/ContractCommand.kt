package com.idrsys.ailis.sales.application.dto.request.contract

import java.time.LocalDate

data class ContractCommand(
    val custCd: String,
    val cntrNo: String?,
    val cntrDt: LocalDate?,
    val cntrStartDt: LocalDate?,
    val cntrEndDt: LocalDate?,
    val cntrType: String?,
    val recntrMonth: String?,
    val cntrNm: String?,
    val cntrCont: String?,
    val cntrPicId: String?,
    val atchGrupId: String,
    val useYn: Boolean = true,
)
