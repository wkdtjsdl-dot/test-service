package com.idrsys.ailis.sales.application.required.repository.charge

import com.idrsys.ailis.sales.application.dto.query.ChargeWithDetails
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface ChargeCustomRepository {
    fun findCharges(searchParam: ChargeSearchParam, pageable: Pageable): Flow<ChargeWithDetails>
    suspend fun countCharge(searchParam: ChargeSearchParam): Long
    suspend fun findChargeWithDetailsById(custChargeId: String): ChargeWithDetails?
}