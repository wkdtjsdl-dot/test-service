package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.DemandHst
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DemandHstDataRepository : CoroutineCrudRepository<DemandHst, String>
