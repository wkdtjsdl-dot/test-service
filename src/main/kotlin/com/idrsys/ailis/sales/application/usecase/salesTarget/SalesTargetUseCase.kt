package com.idrsys.ailis.sales.application.usecase.salesTarget

import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSaveRequest
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalesTargetDetailResponse
import com.idrsys.ailis.sales.application.dto.response.SalesTargetResponse

interface SalesTargetUseCase {
    /**
     * 매출목표 조회 (8.1 API)
     * 년도별 고객별 salesTeamCd별 집계 조회
     */
    suspend fun getSalesTargets(searchParam: SalesTargetSearchParam): List<SalesTargetResponse>

    /**
     * 매출목표 상세 조회 (8.2 API)
     * custCd별 년월별 salesTeamCd별 집계 조회
     */
    suspend fun getSalesTargetDetails(searchParam: SalesTargetDetailSearchParam): List<SalesTargetDetailResponse>

    /**
     * 매출목표 저장 (8.3 API)
     */
    suspend fun saveSalesTargets(request: SalesTargetSaveRequest, adminId: String): List<SalesTargetDetailResponse>
}
