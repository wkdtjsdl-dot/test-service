package com.idrsys.ailis.sales.application.usecase.charge

import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChargeUseCase {
    suspend fun getChargePage(searchParam: ChargeSearchParam, pageable: Pageable): Page<ChargeResponse>
    suspend fun registerCharge(command: ChargeRegisterCommand, creator: String): ChargeResponse
    suspend fun updateCharge(custChargeId: String, command: ChargeUpdateCommand, updater: String): ChargeResponse
    suspend fun getCharge(custChargeId: String): ChargeResponse
    suspend fun getCharges(searchParam: ChargeSearchParam): List<ChargeResponse>
}
