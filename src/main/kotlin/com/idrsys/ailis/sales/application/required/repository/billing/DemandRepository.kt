package com.idrsys.ailis.sales.application.required.repository.billing

import com.idrsys.ailis.sales.application.dto.query.DemandWithCustInfo
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.flow.Flow
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
    suspend fun existsByCustCdAndDemandStartDtAndDemandEndDt(
        custCd: String,
        demandStartDt: LocalDate,
        demandEndDt: LocalDate
    ): Boolean

    // Custom query operations
    /**
     * Find demands (settled)
     */
    fun findDemands(searchParam: DemandSearchParam): Flow<Demand>

    /**
     * Find demands with customer info (JOIN with scs_cust_mst)
     */
    fun findDemandsWithCustInfo(searchParam: DemandSearchParam): Flow<DemandWithCustInfo>

    /**
     * Find demand by ID
     */
    suspend fun findDemandById(demandId: String): Demand?
}
