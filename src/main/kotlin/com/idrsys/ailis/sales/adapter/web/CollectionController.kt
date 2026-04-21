package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateClosingRequest
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateCollectionCommand
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.usecase.collection.CollectionCommandUseCase
import com.idrsys.ailis.sales.application.usecase.collection.CollectionQueryUseCase
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.reactive.excel.ReactiveExcelWriter
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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
    private val collectionQueryUseCase: CollectionQueryUseCase,
    private val excelWriter: ReactiveExcelWriter
) {


    /**
     * Register payment (입금 등록)
     *
     * @param request RegisterCollectionCommand
     * @param auth Authenticated admin from JWT token
     * @return CollectionBillResponse
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registerPayment(
        @RequestBody request: RegisterCollectionCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): CollectionBillResponse {
        return when {
            !request.bankDepositId.isNullOrBlank() && request.cardPayId.isNullOrBlank() -> {
                // 은행 입금만 있음
                collectionCommandUseCase.registerBankDeposit(request, auth.adminId)
            }
            !request.cardPayId.isNullOrBlank() && request.bankDepositId.isNullOrBlank() -> {
                // 카드 결제만 있음
                collectionCommandUseCase.registerCardPayment(request, auth.adminId)
            }
            request.bankDepositId.isNullOrBlank() && request.cardPayId.isNullOrBlank() -> {
                // 둘 다 없음 → 현금/어음
                collectionCommandUseCase.registerCashOrBillPayment(request, auth.adminId)
            }
            else -> {
                // 둘 다 있거나 예상치 못한 경우
                throw IllegalArgumentException(
                    "잘못된 요청입니다. bankDepositId: ${request.bankDepositId}, cardPayId: ${request.cardPayId}"
                )
            }
        }
    }
    @Operation(summary = "입금 수정", description = "입금 내용 수정")
    @PutMapping("/{colBillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun updatePayment(
        @PathVariable colBillId: String,
        @RequestBody request: UpdateCollectionCommand,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin,
    ): CollectionBillResponse {
        return when {
            !request.bankDepositId.isNullOrBlank() && request.cardPayId.isNullOrBlank() -> {
                // 은행 입금만 있음
                collectionCommandUseCase.updateBankDeposit(colBillId, request, auth.adminId)
            }
            !request.cardPayId.isNullOrBlank() && request.bankDepositId.isNullOrBlank() -> {
                // 카드 결제만 있음
                collectionCommandUseCase.updateCardPayment(colBillId, request, auth.adminId)
            }
            request.bankDepositId.isNullOrBlank() && request.cardPayId.isNullOrBlank() -> {
                // 둘 다 없음 → 현금/어음
                collectionCommandUseCase.updateCashOrBillPayment(colBillId, request, auth.adminId)
            }
            else -> {
                // 둘 다 있거나 예상치 못한 경우
                throw IllegalArgumentException(
                    "잘못된 요청입니다. bankDepositId: ${request.bankDepositId}, cardPayId: ${request.cardPayId}"
                )
            }
        }
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

    @Operation(summary = "입금내역 마감처리", description = "입금내역 마감/마감해제 ")
    @PutMapping("/closing/{colbillId}")
    suspend fun setColbillClosing(
        @PathVariable colbillId: String,
        @RequestBody request: UpdateClosingRequest,
        @Parameter(hidden = true) @JwtAuthorization auth: AuthenticationAdmin
    ): CollectionBillResponse {
        return collectionCommandUseCase.setColbillClosing(colbillId, request,auth.adminId)
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
    ): CollectionLedgerResponse {
        return collectionQueryUseCase.getCollectionLedger(custCd)
    }

    @Operation(summary = "거래내역 엑셀 다운로드")
    @GetMapping("/ledger/{custCd}/excel")
    suspend fun downloadCollectionLedgerExcel(
        @PathVariable custCd: String,
    ): ResponseEntity<Flow<DataBuffer>> {
        val ledger = collectionQueryUseCase.getCollectionLedger(custCd)
        val rows = ledger.transactions.map { t ->
            CollectionLedgerExcelRow(
                colbillDt = t.colbillDt.toString(),
                division = t.division,
                divisionType = if (t.advreceYn) "선수금" else "일반",
                colbillAmt = t.demandAmt,
                collectAmt = t.collectAmt,
                balance = t.balance,
                colbillItemNm = t.colbillItemNm,
            )
        } + CollectionLedgerExcelRow(
            colbillDt = "",
            division = "합계",
            divisionType = "",
            colbillAmt = ledger.totalDemandAmt,
            collectAmt = ledger.totalCollectionAmt,
            balance = null,
            colbillItemNm = null,
        )
        val filename = URLEncoder.encode("거래내역_${LocalDate.now()}.xlsx", StandardCharsets.UTF_8.toString())
        val excelFlow = excelWriter.generateExcel(rows, CollectionLedgerExcelRow::class, "거래내역")
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$filename")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header("Access-Control-Expose-Headers", "Content-Disposition")
            .body(excelFlow)
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
}
