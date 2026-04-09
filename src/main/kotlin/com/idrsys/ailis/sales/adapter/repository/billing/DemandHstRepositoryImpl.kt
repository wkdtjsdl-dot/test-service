package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.application.required.repository.billing.DemandHstRepository
import com.idrsys.ailis.sales.domain.model.DemandHst
import org.springframework.stereotype.Repository

@Repository
class DemandHstRepositoryImpl(
    private val demandHstDataRepository: DemandHstDataRepository
) : DemandHstRepository {

    override suspend fun save(demandHst: DemandHst): DemandHst {
        return demandHstDataRepository.save(demandHst)
    }
}
