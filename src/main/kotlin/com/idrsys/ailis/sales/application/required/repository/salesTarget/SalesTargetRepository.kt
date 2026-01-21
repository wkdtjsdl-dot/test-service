package com.idrsys.ailis.sales.application.required.repository.salesTarget

import com.idrsys.ailis.sales.application.dto.query.SalesTargetDetailQuery
import com.idrsys.ailis.sales.application.dto.query.SalesTargetQuery
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.domain.model.SalesTarget
import kotlinx.coroutines.flow.Flow

/**
 * SalesTarget Repository (Port Interface)
 *
 * Combines basic CRUD operations and complex queries
 */
interface SalesTargetRepository {
    // Basic CRUD operations
    suspend fun save(salesTarget: SalesTarget): SalesTarget
    suspend fun findById(id: String): SalesTarget?
    suspend fun delete(salesTarget: SalesTarget)
    suspend fun findByCustCdAndSalesYearAndSalesMonthAndSalsTeamCd(
        custCd: String,
        salesYear: String,
        salesMonth: String,
        salsTeamCd: String
    ): SalesTarget?

    // Custom query operations
    /**
     * 매출목표 조회 (8.1 API)
     * 년도별 고객별 salesTeamCd별 집계 조회
     * directAcctCd가 있으면 해당 직접거래처에 속한 고객들만 조회
     */
    fun findSalesTargets(searchParam: SalesTargetSearchParam): Flow<SalesTargetQuery>

    /**
     * 매출목표 상세 조회 (8.2 API)
     * custCd별 년월별 salesTeamCd별 집계 조회
     */
    fun findSalesTargetDetails(searchParam: SalesTargetDetailSearchParam): Flow<SalesTargetDetailQuery>
}
