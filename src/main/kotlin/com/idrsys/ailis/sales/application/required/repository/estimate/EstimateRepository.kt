package com.idrsys.ailis.sales.application.required.repository.estimate

import com.idrsys.ailis.sales.domain.model.Estimate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Estimate Repository (Data Interface)
 */
interface EstimateRepository : CoroutineCrudRepository<Estimate, String>
