package com.idrsys.ailis.sales.application.dto.request.charge

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class ChargeRegisterCommand(
    @field:NotBlank
    val custMstId: String,
    @field:NotBlank
    val custCd: String,
    @field:NotNull
    val applyStartDt: LocalDate,
    val applyEndDt: LocalDate? = LocalDate.of(9999, 12, 31),
    @field:NotBlank
    val tstCd: String,
    @field:NotBlank
    val crcyCd: String,
    val stndPrice: Long?,
    @field:NotNull
    val specialCharge: Long,
    val supval: Long?,
    val addtax: Long?,
    val remark: String?,
    @field:NotBlank
    val lastApprStatCd: String
)

data class ExcelChargeRegisterCommand(
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