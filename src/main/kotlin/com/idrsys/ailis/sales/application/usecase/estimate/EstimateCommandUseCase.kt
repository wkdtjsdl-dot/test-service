package com.idrsys.ailis.sales.application.usecase.estimate

import com.idrsys.ailis.sales.application.dto.request.estimate.CreateEstimateCommand
import com.idrsys.ailis.sales.application.dto.request.estimate.UpdateEstimateCommand
import com.idrsys.ailis.sales.application.dto.response.DeleteEstimateResponse
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse

/**
 * Estimate Command Use Case
 *
 * Handles write operations for estimate domain
 */
interface EstimateCommandUseCase {
    /**
     * Create estimate with items
     */
    suspend fun createEstimate(command: CreateEstimateCommand, adminId: String): EstimateResponse

    /**
     * Update estimate with modified items
     */
    suspend fun updateEstimate(estimateId: String, command: UpdateEstimateCommand, adminId: String): EstimateResponse

    /**
     * Delete estimate
     */
    suspend fun deleteEstimate(estimateId: String, adminId: String): DeleteEstimateResponse
}
