package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.request.estimate.CreateEstimateCommand
import com.idrsys.ailis.sales.application.dto.request.estimate.EstimateSearchParam
import com.idrsys.ailis.sales.application.dto.request.estimate.UpdateEstimateCommand
import com.idrsys.ailis.sales.application.dto.response.DeleteEstimateResponse
import com.idrsys.ailis.sales.application.dto.response.EstimateResponse
import com.idrsys.ailis.sales.application.usecase.estimate.EstimateCommandUseCase
import com.idrsys.ailis.sales.application.usecase.estimate.EstimateQueryUseCase
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Estimate Controller
 *
 * REST API endpoints for estimate management (견적서 관리)
 */
@Tag(name = "Estimate", description = "견적서 관리 API")
@RestController
@RequestMapping("/api/estimates")
class EstimateController(
    private val estimateCommandUseCase: EstimateCommandUseCase,
    private val estimateQueryUseCase: EstimateQueryUseCase
) {

    /**
     * Create estimate (견적서 생성)
     *
     * @param request CreateEstimateCommand
     * @param auth Authenticated admin from JWT token
     * @return EstimateResponse
     */
    @Operation(summary = "견적서 생성", description = "견적서/거래명세서 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createEstimate(
        @RequestBody request: CreateEstimateCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): EstimateResponse {
        return estimateCommandUseCase.createEstimate(request, auth.adminId)
    }

    /**
     * Update estimate (견적서 수정)
     *
     * @param estimateId Estimate ID
     * @param request UpdateEstimateCommand
     * @param auth Authenticated admin from JWT token
     * @return EstimateResponse
     */
    @Operation(summary = "견적서 수정", description = "견적서/거래명세서 수정")
    @PutMapping("/{estimateId}")
    suspend fun updateEstimate(
        @PathVariable estimateId: String,
        @RequestBody request: UpdateEstimateCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): EstimateResponse {
        return estimateCommandUseCase.updateEstimate(estimateId, request, auth.adminId)
    }

    /**
     * Delete estimate (견적서 삭제)
     *
     * @param estimateId Estimate ID
     * @param auth Authenticated admin from JWT token
     * @return DeleteEstimateResponse
     */
    @Operation(summary = "견적서 삭제", description = "견적서/거래명세서 삭제")
    @DeleteMapping("/{estimateId}")
    suspend fun deleteEstimate(
        @PathVariable estimateId: String,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): DeleteEstimateResponse {
        return estimateCommandUseCase.deleteEstimate(estimateId, auth.adminId)
    }

    /**
     * Get estimate list (견적서 목록 조회)
     *
     * @param searchParam Search parameters
     * @param pageable Pagination parameters
     * @return Page of EstimateResponse
     */
    @Operation(summary = "견적서 목록 조회", description = "견적서 및 거래명세서 목록 조회")
    @GetMapping
    suspend fun getEstimateList(
        @ModelAttribute searchParam: EstimateSearchParam,
    ): Flow<EstimateResponse> {
        return estimateQueryUseCase.getEstimateList(searchParam)
    }

    /**
     * Get estimate detail (견적서 상세 조회)
     *
     * @param estimateId Estimate ID
     * @return EstimateResponse
     */
    @Operation(summary = "견적서 상세 조회", description = "견적서/거래명세서 상세 정보 조회")
    @GetMapping("/{estimateId}")
    suspend fun getEstimateDetail(
        @PathVariable estimateId: String
    ): EstimateResponse {
        return estimateQueryUseCase.getEstimateDetail(estimateId)
    }
}
