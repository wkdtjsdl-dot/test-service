package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.usecase.collection.CollectionCommandUseCase
import com.idrsys.ailis.sales.application.usecase.collection.CollectionQueryUseCase
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * Collection Controller
 *
 * REST API endpoints for collection management (수금 관리)
 */
@Tag(name = "Collection", description = "수금 관리 API")
@RestController
@RequestMapping("/api/collections")
class CollectionController(
    private val collectionCommandUseCase: CollectionCommandUseCase,
    private val collectionQueryUseCase: CollectionQueryUseCase
) {


    /**
     * Register payment (입금 등록)
     *
     * @param request RegisterCollectionCommand
     * @param auth Authenticated admin from JWT token
     * @return CollectionBillResponse
     */
    @Operation(summary = "입금 등록", description = "카드/은행 입금을 고객에게 매칭하여 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registerPayment(
        @RequestBody request: RegisterCollectionCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): CollectionBillResponse {
        return if (request.cardPayId != null) {
            collectionCommandUseCase.registerCardPayment(request, auth.adminId)
        } else {
            collectionCommandUseCase.registerBankDeposit(request, auth.adminId)
        }
    }

    /**
     * Register split payment (카드 분할 등록)
     *
     * @param request RegisterSplitPaymentCommand
     * @param auth Authenticated admin from JWT token
     * @return SplitCollectionResponse
     */
    @Operation(summary = "카드 분할 등록", description = "하나의 카드 입금을 여러 고객에게 분할 등록")
    @PostMapping("/card-payments/split")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registerSplitCardPayment(
        @RequestBody request: RegisterSplitPaymentCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): SplitCollectionResponse {
        return collectionCommandUseCase.registerSplitPayment(request, auth.adminId)
    }

    /**
     * Register split payment (은행 분할 등록)
     *
     * @param request RegisterSplitPaymentCommand
     * @param auth Authenticated admin from JWT token
     * @return SplitCollectionResponse
     */
    @Operation(summary = "은행 분할 등록", description = "하나의 은행 입금을 여러 고객에게 분할 등록")
    @PostMapping("/bank-deposits/split")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registerSplitBankDeposit(
        @RequestBody request: RegisterSplitPaymentCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): SplitCollectionResponse {
        return collectionCommandUseCase.registerSplitPayment(request, auth.adminId)
    }

    /**
     * Delete collection bill (입금 삭제)
     *
     * @param colbillId Collection bill ID
     * @param auth Authenticated admin from JWT token
     * @return DeleteCollectionBillResponse
     */
    @Operation(summary = "입금 삭제", description = "입금 내역 삭제")
    @DeleteMapping("/{colbillId}")
    suspend fun deleteCollectionBill(
        @PathVariable colbillId: String,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): DeleteCollectionBillResponse {
        return collectionCommandUseCase.deleteCollectionBill(colbillId, auth.adminId)
    }

    /**
     * Send collection to ERP (ERP 전송)
     *
     * @param request SendCollectionToErpCommand
     * @param auth Authenticated admin from JWT token
     * @return SendCollectionResponse
     */
    @Operation(summary = "ERP 전송", description = "선택한 입금 내역을 ERP로 전송")
    @PostMapping("/send-to-erp")
    suspend fun sendCollectionToErp(
        @RequestBody request: SendCollectionToErpCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): SendCollectionResponse {
        return collectionCommandUseCase.sendCollectionToErp(request, auth.adminId)
    }

    /**
     * Get collection ledger (거래내역 조회)
     *
     * @param custCd Customer code
     * @param startDt Start date
     * @param endDt End date
     * @return CollectionLedgerResponse
     */
    @Operation(summary = "거래내역 조회", description = "고객별 청구수금원장 조회 (청구 및 수금 거래내역)")
    @GetMapping("/ledger/{custCd}")
    suspend fun getCollectionLedger(
        @PathVariable custCd: String,
        @RequestParam startDt: LocalDate,
        @RequestParam endDt: LocalDate
    ): CollectionLedgerResponse {
        val searchParam = CollectionSearchParam(
            custCd = custCd,
            startDt = startDt,
            endDt = endDt
        )
        return collectionQueryUseCase.getCollectionLedger(searchParam)
    }

    /**
     * Get card payment list (카드 입금내역 조회)
     *
     * @param startDt Start date
     * @param endDt End date
     * @param payDivCd Payment division code (10: approved, 20: cancelled)
     * @param regYn Registration status
     * @return Flow of CardPaymentResponse
     */
    @Operation(summary = "카드 입금내역 조회", description = "신용카드 입금내역 조회 (VAN사로부터 수신한 데이터)")
    @GetMapping("/card-payments")
    suspend fun getCardPaymentList(
        @RequestParam startDt: String,
        @RequestParam endDt: String,
        @RequestParam(required = false) payDivCd: String?,
        @RequestParam(required = false) regYn: Boolean?
    ): Flow<CardPaymentResponse> {
        val searchParam = CardPaymentSearchParam(
            startDt = startDt,
            endDt = endDt,
            payDivCd = payDivCd,
            regYn = regYn
        )
        return collectionQueryUseCase.getCardPaymentList(searchParam)
    }

    /**
     * Get bank deposit list (은행 입금내역 조회)
     *
     * @param startDt Start date
     * @param endDt End date
     * @param regYn Registration status
     * @return Flow of BankDepositResponse
     */
    @Operation(summary = "은행 입금내역 조회", description = "은행계좌 입금내역 조회 (ERP로부터 수신한 가수금 데이터)")
    @GetMapping("/bank-deposits")
    suspend fun getBankDepositList(
        @RequestParam startDt: LocalDate,
        @RequestParam endDt: LocalDate,
        @RequestParam(required = false) regYn: Boolean?
    ): Flow<BankDepositResponse> {
        val searchParam = BankDepositSearchParam(
            startDt = startDt,
            endDt = endDt,
            regYn = regYn
        )
        return collectionQueryUseCase.getBankDepositList(searchParam)
    }
    @Operation(summary = "Find all collections", description = "입금 목록 조회")
    @GetMapping
    suspend fun getCollectionBills(
        @RequestParam startDt: LocalDate,
        @RequestParam endDt: LocalDate,
        @RequestParam custCd: String?,
        @RequestParam closingCd: String?,
        @RequestParam bzoffiCd: String?,

    ): Flow<CollectionBillListResponse> {
        val searchParam = CollectionListSearchParam(
            custCd = custCd,
            startDt = startDt,
            endDt = endDt,
            closingCd = closingCd,
            bzoffiCd = bzoffiCd
        )
        return collectionCommandUseCase.findCollectionBills(searchParam)
    }

}
