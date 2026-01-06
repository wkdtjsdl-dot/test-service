package com.idrsys.ailis.sales.application.service.billing

import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.DemandType
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.UnsettledDemandSummary
import com.idrsys.ailis.sales.application.required.repository.billing.DemandRepository
import com.idrsys.ailis.sales.application.usecase.billing.BillingQueryUseCase
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Billing Query Service
 *
 * Implements read operations for billing domain
 */
@Service
@Transactional(readOnly = true)
class BillingQueryService(
    private val demandRepository: DemandRepository,
) : BillingQueryUseCase {

    /**
     * Get demand list (settled or unsettled)
     *
     * Business Rules:
     * 1. SETTLED: Demands that have been created (청구 완료)
     * 2. UNSETTLED: Test requests not yet included in demands (미청구)
     */
    override suspend fun getDemandList(searchParam: DemandSearchParam, pageable: Pageable): Page<*> {
        return when (searchParam.demandType) {
            DemandType.SETTLED -> {
                // Get created demands
                val total = demandRepository.countDemands(searchParam)
                if (total == 0L) return PageImpl(emptyList<DemandResponse>(), pageable, 0)

                val demands = demandRepository.findDemands(searchParam, pageable)
                    .map { DemandResponse.from(it) }
                    .toList()
                PageImpl(demands, pageable, total)
            }
            DemandType.UNSETTLED -> {
                // Get unsettled demand summary (grouped by customer)
                getUnsettledDemandSummary(searchParam, pageable)
            }
        }
    }

    /**
     * Get demand detail
     */
    override suspend fun getDemandDetail(demandId: String): DemandResponse {
        val demand = demandRepository.findById(demandId)
            ?: throw UserDefinedException("DEMAND_NOT_FOUND", "청구서를 찾을 수 없습니다: $demandId")

        return DemandResponse.from(demand)
    }

    /**
     * Get unsettled demand summary (grouped by customer)
     *
     * Business Rules:
     * 1. Only include test requests with closingCd = "CLCD_N"
     * 2. Aggregate amounts per customer
     * 3. Count number of requests per customer
     */
    override suspend fun getUnsettledDemandSummary(
        searchParam: DemandSearchParam,
        pageable: Pageable
    ): Page<UnsettledDemandSummary> {
        val total = demandRepository.countUnsettledDemandSummary(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val summaries = demandRepository.findUnsettledDemandSummary(searchParam, pageable).toList()
        return PageImpl(summaries, pageable, total)
    }
}
