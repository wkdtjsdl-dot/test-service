package com.idrsys.ailis.sales.application.dto.request.charge

import java.time.LocalDate

data class ChargeRegisterCommand(
    val custMstId: String,
    val custCd: String,
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate? = LocalDate.of(9999, 12, 31),
    val tstCd: String,
    val crcyCd: String,
    val stndPrice: Long?,
    val specialCharge: Long,
    val supval: Long?,
    val addtax: Long?,
    val remark: String?,
    val lastApprStatCd: String
)

data class ChargeUpdateCommand(
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate,
    val crcyCd: String,
    val specialCharge: Long,
    val supval: Long?,
    val addtax: Long?,
    val remark: String?
)