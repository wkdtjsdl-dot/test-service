package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.query.SalesTargetDetailQuery
import com.idrsys.ailis.sales.application.dto.query.SalesTargetQuery
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetDetailSearchParam
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSaveRequest
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalesTargetDetailResponse
import com.idrsys.ailis.sales.application.dto.response.SalesTargetResponse
import com.idrsys.ailis.sales.application.required.repository.salesTarget.SalesTargetRepository
import com.idrsys.ailis.sales.application.usecase.salesTarget.SalesTargetUseCase
import com.idrsys.ailis.sales.domain.model.SalesTarget
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class SalesTargetService(
    private val salesTargetRepository: SalesTargetRepository,
) : SalesTargetUseCase {

    override suspend fun getSalesTargets(searchParam: SalesTargetSearchParam): List<SalesTargetResponse> {
        return salesTargetRepository.findSalesTargets(searchParam)
            .toList()
            .map { it.toResponse() }
    }

    override suspend fun getSalesTargetDetails(searchParam: SalesTargetDetailSearchParam): List<SalesTargetDetailResponse> {
        return salesTargetRepository.findSalesTargetDetails(searchParam)
            .toList()
            .map { it.toResponse() }
    }

    override suspend fun saveSalesTargets(request: SalesTargetSaveRequest, adminId: String): List<SalesTargetDetailResponse> {
        val now = LocalDateTime.now()
        val salesYear = request.year.toString()

        request.monthlyTargets.forEach { item ->
            val salesMonth = item.month.toString().padStart(2, '0')

            val existing = salesTargetRepository.findByCustCdAndSalesYearAndSalesMonthAndSalsTeamCd(
                custCd = request.custCd,
                salesYear = salesYear,
                salesMonth = salesMonth,
                salsTeamCd = item.salsTeamCd
            )

            if (existing != null) {
                existing.update(
                    monthSalesTargetAmt = item.monthlyTarget,
                    pastYearMonthSalesAmt = item.prevYearSales,
                    updater = adminId
                )
                salesTargetRepository.save(existing)
            } else {
                val newTarget = SalesTarget(
                    custCd = request.custCd,
                    salesYear = salesYear,
                    salesMonth = salesMonth,
                    salsTeamCd = item.salsTeamCd,
                    monthSalesTargetAmt = item.monthlyTarget,
                    pastYearMonthSalesAmt = item.prevYearSales,
                    creator = adminId,
                    createDtime = now,
                    updater = adminId,
                    updateDtime = now
                ).apply { setAsNew() }
                salesTargetRepository.save(newTarget)
            }
        }

        return getSalesTargetDetails(
            SalesTargetDetailSearchParam(
                year = request.year,
                custCd = request.custCd
            )
        )
    }

    private fun SalesTargetQuery.toResponse(): SalesTargetResponse {
        val growthRate = if (prevYearSales > BigDecimal.ZERO) {
            (totalTarget - prevYearSales)
                .multiply(BigDecimal(100))
                .divide(prevYearSales, 2, RoundingMode.HALF_UP)
        } else {
            null
        }

        return SalesTargetResponse(
            rowId = rowId,
            year = salesYear.toInt(),
            custCd = custCd,
            custNm = custNm,
            salesTeamCd = salsTeamCd,
            salesTeamNm = salsTeamNm,
            totalTarget = totalTarget,
            prevYearSales = prevYearSales,
            targetGrowthRate = growthRate
        )
    }

    private fun SalesTargetDetailQuery.toResponse(): SalesTargetDetailResponse {
        return SalesTargetDetailResponse(
            rowId = rowId,
            year = salesYear.toInt(),
            custCd = custCd,
            custNm = custNm,
            salesTeamCd = salsTeamCd,
            salesTeamNm = salsTeamNm,
            month = salesMonth.toInt(),
            monthlyTarget = monthlyTarget,
            prevYearSales = prevYearSales
        )
    }
}
