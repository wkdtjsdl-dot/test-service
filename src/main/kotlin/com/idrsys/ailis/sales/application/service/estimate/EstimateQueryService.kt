package com.idrsys.ailis.sales.application.service.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateItemRepository
import com.idrsys.ailis.sales.application.required.repository.estimate.EstimateRepository
import com.idrsys.ailis.sales.application.usecase.estimate.EstimateQueryUseCase
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Estimate Query Service
 *
 * Implements read operations for estimate domain
 */
@Service
@Transactional(readOnly = true)
class EstimateQueryService(
    private val estimateRepository: EstimateRepository,
    private val estimateItemRepository: EstimateItemRepository,
) : EstimateQueryUseCase {

    /**
     * Get estimate list
     */
    override suspend fun getEstimateList(
        searchParam: EstimateSearchParam,
        pageable: Pageable
    ): Page<EstimateResponse> {
        val total = estimateRepository.countEstimates(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val estimates = estimateRepository.findEstimates(searchParam, pageable)
            .map { estimate ->
                val items = estimateItemRepository.findByEstimateId(estimate.estimateId!!).toList()
                EstimateResponse.from(estimate, items)
            }
            .toList()
        return PageImpl(estimates, pageable, total)
    }

    /**
     * Get estimate detail
     */
    override suspend fun getEstimateDetail(estimateId: String): EstimateResponse {
        // 1. Find estimate
        val estimate = estimateRepository.findById(estimateId)
            ?: throw UserDefinedException("ESTIMATE_NOT_FOUND", "견적서를 찾을 수 없습니다: $estimateId")

        // 2. Find items
        val items = estimateItemRepository.findByEstimateId(estimateId).toList()

        // 3. Return response
        return EstimateResponse.from(estimate, items)
    }
}
