package com.idrsys.ailis.sales.adapter.repository.salesTarget

import com.idrsys.ailis.sales.domain.model.SalesTarget
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * SalesTargetDataRepository
 *
 * R2DBC reactive repository for SalesTarget entity
 */
@Repository
interface SalesTargetDataRepository : CoroutineCrudRepository<SalesTarget, String> {

    /**
     * Find sales target by custCd, salesYear, salesMonth, and salsTeamCd
     */
    suspend fun findByCustCdAndSalesYearAndSalesMonthAndSalsTeamCd(
        custCd: String,
        salesYear: String,
        salesMonth: String,
        salsTeamCd: String
    ): SalesTarget?
}
