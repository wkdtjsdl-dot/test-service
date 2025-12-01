package com.idrsys.ailis.sales.application.required.repository.charge

import com.idrsys.ailis.sales.domain.model.Charge

interface ChargeRepository {
    suspend fun save(charge: Charge): Charge
    suspend fun findById(custChargeId: String): Charge?
}
