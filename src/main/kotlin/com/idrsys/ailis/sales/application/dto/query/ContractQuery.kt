package com.idrsys.ailis.sales.application.dto.query

import java.time.LocalDate
import java.time.LocalDateTime

data class ContractWithDetails(
    val custCntrId: Long,
    val custMstId: String,
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
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime,

    // Joined fields
    val custNm: String?,
    val cntrPicNm: String?
)
