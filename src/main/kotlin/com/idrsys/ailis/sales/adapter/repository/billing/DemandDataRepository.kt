package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * DemandDataRepository
 *
 * R2DBC reactive repository for Demand entity
 */
@Repository
interface DemandDataRepository : CoroutineCrudRepository<Demand, String> {

    /**
     * Find demands by customer code
     */
    fun findByCustCd(custCd: String): Flow<Demand>

    /**
     * Find demands by period (demand standard date between)
     */
    fun findByDemandEndDtBetween(startDate: LocalDate, endDate: LocalDate): Flow<Demand>

    /**
     * Find demands by customer code and period
     */
    fun findByCustCdAndDemandEndDtBetween(
        custCd: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Demand>

    /**
     * Check if demand exists by customer code, demand start date, and demand standard date
     */
    suspend fun existsByCustCdAndDemandStartDtAndDemandEndDt(
        custCd: String,
        demandStartDt: LocalDate,
        demandEndDt: LocalDate
    ): Boolean
}
