package com.idrsys.ailis.sales.application.required.repository.billing

import com.idrsys.ailis.sales.domain.model.DemandHst

interface DemandHstRepository {
    suspend fun save(demandHst: DemandHst): DemandHst
}
