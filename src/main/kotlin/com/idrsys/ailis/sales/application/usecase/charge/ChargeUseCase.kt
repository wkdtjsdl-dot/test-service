package com.idrsys.ailis.sales.application.usecase.charge

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.request.charge.ExcelChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.dto.response.ExcelRegisterValidationResponse
import com.idrsys.ailis.sales.application.dto.response.inner.CustChargeInnerResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface ChargeUseCase {
    suspend fun getChargePage(searchParam: ChargeSearchParam, pageable: Pageable, isExcel: Boolean = false): Page<ChargeResponse>
    suspend fun registerCharge(command: ChargeRegisterCommand, creator: String): ChargeResponse
    suspend fun updateCharge(custChargeId: String, command: ChargeUpdateCommand, updater: String): ChargeResponse
    suspend fun deleteCharge(custChargeId: String)
    suspend fun getCharge(custChargeId: String): ChargeResponse
    suspend fun getCharges(searchParam: ChargeSearchParam, isExcel: Boolean = false): List<ChargeResponse>
    suspend fun validateExcelRegisterCharges(commands: List<ExcelChargeRegisterCommand>): ExcelRegisterValidationResponse
    suspend fun excelRegisterCharges(commands: List<ExcelChargeRegisterCommand>, userId: String): List<ChargeResponse>

    // Inner API: 청구수가 재계산용 고객수가 조회
    suspend fun getCustChargesForBilling(
        custCds: List<String>,
        tstCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate
    ): List<CustChargeInnerResponse>
}
