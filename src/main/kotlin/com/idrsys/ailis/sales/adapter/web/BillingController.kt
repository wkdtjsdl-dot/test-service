package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.DemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementCommand
import java.time.LocalDate
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementResponse
import com.idrsys.ailis.sales.application.usecase.billing.BillingCommandUseCase
import com.idrsys.ailis.sales.application.usecase.billing.BillingQueryUseCase
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Billing Controller
 *
 * REST API endpoints for billing management (청구 관리)
 */
@Tag(name = "Billing", description = "청구 관리 API")
@RestController
@RequestMapping("/api/billing")
class BillingController(
    private val billingCommandUseCase: BillingCommandUseCase,
    private val billingQueryUseCase: BillingQueryUseCase
) {

    /**
     * Create demand (청구서 생성 마감)
     *
     * @param request CreateDemandCommand
     * @param auth Authenticated admin from JWT token
     * @return CreateDemandResponse with created demand ID
     */
    @Operation(summary = "청구서 생성", description = "미청구 건을 마감하여 청구서 생성")
    @PostMapping("/demands")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createDemand(
        @RequestBody request: CreateDemandCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): CreateDemandResponse {
        return billingCommandUseCase.createDemand(request, auth.adminId)
    }

    /**
     * Cancel demand (청구 취소)
     *
     * @param demandId Demand ID to cancel
     * @param auth Authenticated admin from JWT token
     * @return CancelDemandResponse with cancelled flag
     */
    @Operation(summary = "청구서 취소", description = "청구 마감 취소 (매출전표 생성 전에만 가능)")
    @DeleteMapping("/demands/{demandId}")
    suspend fun cancelDemand(
        @PathVariable demandId: String,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): CancelDemandResponse {
        return billingCommandUseCase.cancelDemand(demandId, auth.adminId)
    }

    /**
     * Send sales statement to ERP (매출전표 생성)
     *
     * @param demandId Demand ID
     * @param auth Authenticated admin from JWT token
     * @return SendSalesStatementResponse with statement number
     */
    @Operation(summary = "매출전표 생성", description = "청구 데이터를 ERP로 전송하여 매출전표 생성")
    @PostMapping("/demands/{demandId}/sales-statements")
    suspend fun sendSalesStatement(
        @PathVariable demandId: String,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): SendSalesStatementResponse {
        return billingCommandUseCase.sendSalesStatement(
            SendSalesStatementCommand(demandId),
            auth.adminId
        )
    }

    /**
     * Get demand list (청구 리스트 조회)
     *
     * Returns unified DemandResponse type for both SETTLED and UNSETTLED cases
     *
     * @param CLCD Demand type (SETTLED or UNSETTLED)
     * @param startDt Start date
     * @param endDt End date
     * @param custCd Customer code (optional)
     * @param branchCd Branch code (optional)
     * @return Flow of DemandResponse
     */
    @Operation(summary = "청구 리스트 조회", description = "청구 마감된 리스트 또는 미청구 리스트 조회")
    @GetMapping("/demands")
    fun getDemandList(
        @ParameterObject searchParam: DemandSearchParam,
    ): Flow<DemandResponse> {
        return billingQueryUseCase.getDemandList(searchParam)
    }

    /**
     * Get demand detail (청구 상세 조회)
     *
     * @param demandId Demand ID
     * @return DemandResponse
     */
    @Operation(summary = "청구 상세 조회", description = "특정 청구의 상세 정보 조회")
    @GetMapping("/demands/{demandId}")
    suspend fun getDemandDetail(
        @PathVariable demandId: String
    ): ResponseEntity<DemandResponse> {
        return billingQueryUseCase.getDemandDetail(demandId)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}
