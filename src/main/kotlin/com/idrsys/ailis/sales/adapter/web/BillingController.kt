package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.request.billing.BillingRequestSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.application.dto.request.billing.DemandBaseSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.toDemandSearchParam
import com.idrsys.ailis.sales.application.dto.request.billing.CLCD
import com.idrsys.ailis.sales.application.dto.request.billing.RecalculateBillingCommand
import com.idrsys.ailis.sales.application.dto.request.billing.SendSalesStatementBatchCommand
import com.idrsys.ailis.sales.application.dto.response.RecalculateBillingResponse
import com.idrsys.ailis.sales.application.dto.response.SendSalesStatementBatchResponse
import java.time.LocalDate
import com.idrsys.ailis.sales.application.dto.response.BillingRequestResponse
import com.idrsys.ailis.sales.application.dto.response.CancelDemandResponse
import com.idrsys.ailis.sales.application.dto.response.CreateDemandResponse
import com.idrsys.ailis.sales.application.dto.response.DemandResponse
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

    @Operation(summary = "매출전표 배치 생성", description = "선택한 청구 건을 1회 RFC 호출로 일괄 ERP 전송")
    @PostMapping("/demands/sales-statements/batch")
    suspend fun sendSalesStatementBatch(
        @RequestBody command: SendSalesStatementBatchCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): SendSalesStatementBatchResponse {
        return billingCommandUseCase.sendSalesStatementBatch(command, auth.adminId)
    }

    /**
     * Get demand list (청구 리스트 조회 - 전체)
     *
     * Returns unified DemandResponse type for both SETTLED and UNSETTLED cases
     * Includes both domestic and foreign accounts (frgnAcctYn = null)
     *
     * @param searchParam DemandBaseSearchParam (without frgnAcctYn)
     * @return Flow of DemandResponse
     */
    @Operation(summary = "청구 리스트 조회", description = "전체 청구 리스트 조회 (국내+해외)")
    @GetMapping("/demands")
    fun getDemandList(
        @ParameterObject searchParam: DemandBaseSearchParam,
    ): Flow<DemandResponse> {
        return billingQueryUseCase.getDemandList(searchParam.toDemandSearchParam(frgnAcctYn = null))
    }

    /**
     * Get domestic demand list (국내 청구 리스트 조회)
     *
     * Returns DemandResponse for domestic accounts only (frgnAcctYn = false)
     *
     * @param searchParam DemandBaseSearchParam (without frgnAcctYn)
     * @return Flow of DemandResponse
     */
    @Operation(summary = "국내 청구 리스트 조회", description = "국내 거래처 청구 리스트 조회")
    @GetMapping("/demands/domestic")
    fun getDemandListDomestic(
        @ParameterObject searchParam: DemandBaseSearchParam,
    ): Flow<DemandResponse> {
        return billingQueryUseCase.getDemandList(searchParam.toDemandSearchParam(frgnAcctYn = false))
    }

    /**
     * Get foreign demand list (해외 청구 리스트 조회)
     *
     * Returns DemandResponse for foreign accounts only (frgnAcctYn = true)
     *
     * @param searchParam DemandBaseSearchParam (without frgnAcctYn)
     * @return Flow of DemandResponse
     */
    @Operation(summary = "해외 청구 리스트 조회", description = "해외 거래처 청구 리스트 조회")
    @GetMapping("/demands/foreign")
    fun getDemandListForeign(
        @ParameterObject searchParam: DemandBaseSearchParam,
    ): Flow<DemandResponse> {
        return billingQueryUseCase.getDemandList(searchParam.toDemandSearchParam(frgnAcctYn = true))
    }

    /**
     * Get billing request details (의뢰내역 조회)
     *
     * @param searchParam BillingRequestSearchParam
     * @return Flow of BillingRequestResponse
     */
    @Operation(summary = "청구수가 재마감", description = "해당 월 CLCD_Y 의뢰 기준으로 sbl_demand 재산출 (tst-item 미변경)")
    @PostMapping("/demands/recalculate")
    suspend fun recalculateBillingDemands(
        @RequestBody command: RecalculateBillingCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): RecalculateBillingResponse {
        return billingCommandUseCase.recalculateBillingDemands(command, auth.adminId)
    }

    @Operation(summary = "의뢰내역 조회", description = "청구 대상 의뢰내역 조회 (미청구 건의 원본 데이터)")
    @GetMapping("/requests")
    fun getBillingRequests(
        @ParameterObject searchParam: BillingRequestSearchParam
    ): Flow<BillingRequestResponse> {
        return billingQueryUseCase.getBillingRequests(searchParam)
    }
}
