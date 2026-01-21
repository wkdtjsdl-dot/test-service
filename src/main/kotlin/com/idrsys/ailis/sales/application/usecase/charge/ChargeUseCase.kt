package com.idrsys.ailis.sales.application.usecase.charge

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.request.charge.ExcelChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.ExcelRegisterValidationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChargeUseCase {
    suspend fun getChargePage(searchParam: ChargeSearchParam, pageable: Pageable): Page<ChargeResponse>
    suspend fun registerCharge(command: ChargeRegisterCommand, creator: String): ChargeResponse
    suspend fun updateCharge(custChargeId: String, command: ChargeUpdateCommand, updater: String): ChargeResponse
    suspend fun deleteCharge(custChargeId: String)
    suspend fun getCharge(custChargeId: String): ChargeResponse
    suspend fun getCharges(searchParam: ChargeSearchParam): List<ChargeResponse>
    suspend fun validateExcelRegisterCharges(commands: List<ExcelChargeRegisterCommand>): ExcelRegisterValidationResponse
    suspend fun excelRegisterCharges(commands: List<ExcelChargeRegisterCommand>, userId: String): List<ChargeResponse>
}
