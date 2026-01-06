package com.idrsys.ailis.sales.application.required.repository.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import java.time.LocalDate

/**
 * Demand Repository (Port Interface)
 *
 * Combines basic CRUD operations and complex queries
 */
interface DemandRepository {
    // Basic CRUD operations
    suspend fun save(demand: Demand): Demand
    suspend fun findById(id: String): Demand?
    suspend fun delete(demand: Demand)
    suspend fun existsByCustCdAndDemandStartDtAndDemandStndDt(
        custCd: String,
        demandStartDt: LocalDate,
        demandStndDt: LocalDate
    ): Boolean

    // Custom query operations
    /**
     * Find demands (settled) with pagination
     */
    fun findDemands(searchParam: DemandSearchParam, pageable: Pageable): Flow<Demand>

    /**
     * Count demands (settled)
     */
    suspend fun countDemands(searchParam: DemandSearchParam): Long

    /**
     * Find demand by ID
     */
    suspend fun findDemandById(demandId: String): Demand?

    /**
     * Find unsettled demand summary (grouped by customer)
     */
    fun findUnsettledDemandSummary(searchParam: DemandSearchParam, pageable: Pageable): Flow<UnsettledDemandSummary>

    /**
     * Count unsettled demand summary
     */
    suspend fun countUnsettledDemandSummary(searchParam: DemandSearchParam): Long
}
