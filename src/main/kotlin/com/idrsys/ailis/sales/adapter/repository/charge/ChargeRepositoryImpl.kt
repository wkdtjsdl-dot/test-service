package com.idrsys.ailis.sales.adapter.repository.charge

import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository
import com.idrsys.ailis.sales.domain.model.Charge
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChargeDataRepository : CoroutineCrudRepository<Charge, String>

@Repository
class ChargeRepositoryImpl(
    private val chargeDataRepository: ChargeDataRepository
) : ChargeRepository {
    override suspend fun save(charge: Charge): Charge {
        return chargeDataRepository.save(charge)
    }

    override suspend fun findById(custChargeId: String): Charge? {
        return chargeDataRepository.findById(custChargeId)
    }

    override suspend fun deleteById(custChargeId: String) {
        chargeDataRepository.deleteById(custChargeId)
    }
}
