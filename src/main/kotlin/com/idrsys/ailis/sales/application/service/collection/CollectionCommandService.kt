package com.idrsys.ailis.sales.application.service.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.DeleteCollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResult
import com.idrsys.ailis.sales.application.dto.response.SplitCollectionResponse
import com.idrsys.ailis.sales.application.required.repository.collection.*
import com.idrsys.ailis.sales.application.usecase.collection.CollectionCommandUseCase
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.String

/**
 * Collection Command Service
 *
 * Implements write operations for collection domain following TDD approach
 */
@Service
@Transactional
class CollectionCommandService(
    private val collectionBillRepository: CollectionBillRepository,
    private val collectionLedgerRepository: CollectionLedgerRepository,
    private val cardPaymentRepository: CardPaymentRepository,
    private val bankDepositRepository: BankDepositRepository,
) : CollectionCommandUseCase {

    override suspend fun findCollectionBills(searchParam: CollectionListSearchParam): Flow<CollectionBillListResponse> {
        return collectionBillRepository.findCollectionBills(searchParam)
    }
    /**
     * Register card payment to customer
     *
     * Business Rules:
     * 1. Card payment can only be registered once
     * 2. Create collection bill and ledger entry
     * 3. Mark card payment as registered
     */
    override suspend fun registerCardPayment(
        command: RegisterCollectionCommand,
        adminId: String
    ): CollectionBillResponse {
        // 1. Validate card payment exists and not registered
        val cardPayId = command.cardPayId
            ?: throw UserDefinedException("INVALID_REQUEST", "카드 결제 ID가 필요합니다")

        val cardPayment = cardPaymentRepository.findById(cardPayId)
            ?: throw UserDefinedException("CARD_PAYMENT_NOT_FOUND", "카드 결제를 찾을 수 없습니다: $cardPayId")

        if (cardPayment.regYn == true) {
            throw UserDefinedException("DUPLICATE_REGISTRATION", "이미 등록된 카드 결제입니다: $cardPayId")
        }

        // 2. Create collection bill
        val collectionBill = CollectionBill(
            colbillId = null,
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            payMethodCd = command.payMethodCd,
            payAmt = command.payAmt,
            cardPayId = command.cardPayId,
            cardApprNo = command.cardApprNo,
            cardNo = command.cardNo,
            cardBillNo = command.cardBillNo,
            bankDepositId = null,
            accountYear = null,
            surecpSlstmtNo = null,
            advreceYn = command.advreceYn,
            sendYn = false,
            remark = command.remark,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        )
        collectionBill.setAsNew()

        val savedBill = collectionBillRepository.save(collectionBill)

        // 3. Create collection ledger
        val ledger = CollectionLedger.createForCollection(
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            colbillItemNm = "결제(카드)",
            payAmt = command.payAmt,
            creator = adminId
        )

        val savedLedger = collectionLedgerRepository.save(ledger)

        // 4. Mark card payment as registered
        val updatedCardPayment = cardPayment.copy(regYn = true)
        cardPaymentRepository.save(updatedCardPayment)

        // 5. Return response
        return CollectionBillResponse.from(savedBill, savedLedger.colledgerId)
    }

    /**
     * Register bank deposit to customer
     *
     * Business Rules:
     * 1. Bank deposit can only be registered once
     * 2. Create collection bill and ledger entry
     * 3. Mark bank deposit as registered
     */
    override suspend fun registerBankDeposit(
        command: RegisterCollectionCommand,
        adminId: String
    ): CollectionBillResponse {
        // 1. Validate bank deposit exists and not registered
        val bankDepositId = command.bankDepositId
            ?: throw UserDefinedException("INVALID_REQUEST", "은행 입금 ID가 필요합니다")

        val bankDeposit = bankDepositRepository.findById(bankDepositId)
            ?: throw UserDefinedException("BANK_DEPOSIT_NOT_FOUND", "은행 입금을 찾을 수 없습니다: $bankDepositId")

        if (bankDeposit.regYn == true) {
            throw UserDefinedException("DUPLICATE_REGISTRATION", "이미 등록된 은행 입금입니다: $bankDepositId")
        }

        // 2. Create collection bill
        val collectionBill = CollectionBill(
            colbillId = null,
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            payMethodCd = command.payMethodCd,
            payAmt = command.payAmt,
            cardPayId = null,
            cardApprNo = null,
            cardNo = null,
            cardBillNo = null,
            bankDepositId = command.bankDepositId,
            accountYear = command.accountYear,
            surecpSlstmtNo = command.surecpSlstmtNo,
            advreceYn = command.advreceYn,
            sendYn = false,
            remark = command.remark,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        )
        collectionBill.setAsNew()



        val savedBill = collectionBillRepository.save(collectionBill)
        collectionBill.setAsExisting()

        // 3. Create collection ledger
        val ledger = CollectionLedger.createForCollection(
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            colbillItemNm = "결제(은행)",
            payAmt = command.payAmt,
            creator = adminId
        )

        val savedLedger = collectionLedgerRepository.save(ledger)

        // 4. Mark bank deposit as registered
        val updatedBankDeposit = bankDeposit.copy(regYn = true)
        bankDepositRepository.save(updatedBankDeposit)

        // 5. Return response
        return CollectionBillResponse.from(savedBill, savedLedger.colledgerId)
    }

    /**
     * Register split payment (card or bank) to multiple customers
     *
     * Business Rules:
     * 1. Sum of split amounts must exactly match payment amount
     * 2. Each customer receives separate bill and ledger entry
     * 3. No duplicate customers in splits
     */
    override suspend fun registerSplitPayment(
        command: RegisterSplitPaymentCommand,
        adminId: String
    ): SplitCollectionResponse {
        // 1. Validate splits
        if (command.splits.isEmpty()) {
            throw UserDefinedException("INVALID_REQUEST", "최소 1개 이상의 분할 정보가 필요합니다")
        }

        // Check for duplicate customers
        val customerCodes = command.splits.map { it.custCd }
        if (customerCodes.distinct().size != customerCodes.size) {
            throw UserDefinedException("DUPLICATE_CUSTOMER", "중복된 고객이 포함되어 있습니다")
        }

        // 2. Get payment source (card or bank)
        val (totalPayAmt, paymentType) = if (command.cardPayId != null) {
            val cardPayment = cardPaymentRepository.findById(command.cardPayId)
                ?: throw UserDefinedException("CARD_PAYMENT_NOT_FOUND", "카드 결제를 찾을 수 없습니다: ${command.cardPayId}")

            if (cardPayment.regYn == true) {
                throw UserDefinedException("DUPLICATE_REGISTRATION", "이미 등록된 카드 결제입니다: ${command.cardPayId}")
            }

            cardPayment.payAmt to "CARD"
        } else if (command.bankDepositId != null) {
            val bankDeposit = bankDepositRepository.findById(command.bankDepositId)
                ?: throw UserDefinedException("BANK_DEPOSIT_NOT_FOUND", "은행 입금을 찾을 수 없습니다: ${command.bankDepositId}")

            if (bankDeposit.regYn == true) {
                throw UserDefinedException("DUPLICATE_REGISTRATION", "이미 등록된 은행 입금입니다: ${command.bankDepositId}")
            }

            bankDeposit.depositAmt to "BANK"
        } else {
            throw UserDefinedException("INVALID_REQUEST", "카드 결제 또는 은행 입금 ID가 필요합니다")
        }

        // 3. Validate split total matches payment amount
        val splitTotal = command.splits.sumOf { it.payAmt }
        if (splitTotal != totalPayAmt) {
            throw UserDefinedException(
                "SPLIT_TOTAL_MISMATCH",
                "분할 금액 합계($splitTotal)가 결제 금액($totalPayAmt)과 일치하지 않습니다"
            )
        }

        // 4. Create collection bills and ledgers for each split
        val collectionBills = mutableListOf<CollectionBillResponse>()

        command.splits.forEach { split ->
            val collectionBill = CollectionBill(
                colbillId = null,
                custCd = split.custCd,
                colbillDt = split.colbillDt,
                payMethodCd = paymentType,
                payAmt = split.payAmt,
                cardPayId = command.cardPayId,
                bankDepositId = command.bankDepositId,
                advreceYn = false,
                sendYn = false,
                creator = adminId,
                createDtime = LocalDateTime.now(),
                updater = adminId,
                updateDtime = LocalDateTime.now()
            )
            collectionBill.setAsNew()

            val savedBill = collectionBillRepository.save(collectionBill)

            val ledger = CollectionLedger.createForCollection(
                custCd = split.custCd,
                colbillDt = split.colbillDt,
                colbillItemNm = "결제(${if (paymentType == "CARD") "카드" else "은행"})",
                payAmt = split.payAmt,
                creator = adminId
            )

            collectionLedgerRepository.save(ledger)

            collectionBills.add(
                CollectionBillResponse.from(savedBill)
            )
        }

        // 5. Mark payment source as registered
        if (command.cardPayId != null) {
            val cardPayment = cardPaymentRepository.findById(command.cardPayId)!!
            cardPaymentRepository.save(cardPayment.copy(regYn = true))
        } else {
            val bankDeposit = bankDepositRepository.findById(command.bankDepositId!!)!!
            bankDepositRepository.save(bankDeposit.copy(regYn = true))
        }

        // 6. Return response
        return SplitCollectionResponse(
            cardPayId = command.cardPayId?.toString(),
            bankDepositId = command.bankDepositId?.toString(),
            totalPayAmt = totalPayAmt,
            splitCount = command.splits.size,
            colbills = collectionBills
        )
    }

    /**
     * Delete collection bill
     *
     * Business Rules:
     * 1. Can only delete bills not sent to ERP
     * 2. Payment source becomes available for re-registration
     * 3. Associated ledger is also deleted
     */
    override suspend fun deleteCollectionBill(colbillId: String, adminId: String): DeleteCollectionBillResponse {
        // 1. Find collection bill
        val collectionBill = collectionBillRepository.findById(colbillId)
            ?: throw UserDefinedException("COLLECTION_BILL_NOT_FOUND", "수금 정보를 찾을 수 없습니다: $colbillId")

        // 2. Check if already sent to ERP
        if (collectionBill.sendYn) {
            throw UserDefinedException("INVALID_OPERATION", "ERP 전송된 수금 정보는 삭제할 수 없습니다")
        }

        // 3. Delete associated ledger using colledgerId
        collectionBill.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
            if (ledger != null) {
                collectionLedgerRepository.delete(ledger)
            }
        }

        // 4. Mark payment source as not registered
        if (collectionBill.cardPayId != null) {
            val cardPayment = cardPaymentRepository.findById(collectionBill.cardPayId.toString())
            if (cardPayment != null) {
                cardPaymentRepository.save(cardPayment.copy(regYn = false))
            }
        } else if (collectionBill.bankDepositId != null) {
            val bankDeposit = bankDepositRepository.findById(collectionBill.bankDepositId.toString())
            if (bankDeposit != null) {
                bankDepositRepository.save(bankDeposit.copy(regYn = false))
            }
        }

        // 5. Delete collection bill
        collectionBillRepository.delete(collectionBill)

        // 6. Return response
        return DeleteCollectionBillResponse(
            colbillId = colbillId,
            deleted = true
        )
    }

    /**
     * Send collection bills to ERP
     *
     * Business Rules:
     * 1. Bills can only be sent once
     * 2. Individual bill failures don't affect others
     */
    override suspend fun sendCollectionToErp(
        command: SendCollectionToErpCommand,
        adminId: String
    ): SendCollectionResponse {
        val results = command.colbillIds.map { colbillId ->
            try {
                val bill = collectionBillRepository.findById(colbillId)
                    ?: throw UserDefinedException("COLLECTION_BILL_NOT_FOUND", "수금 정보를 찾을 수 없습니다: $colbillId")

                if (bill.sendYn) {
                    SendCollectionResult(
                        colbillId = colbillId,
                        sent = false,
                        message = "이미 전송된 수금 정보입니다"
                    )
                } else {
                    // TODO: Call ERP integration service

                    // Mark as sent
                    bill.markAsSent(adminId)
                    collectionBillRepository.save(bill)

                    SendCollectionResult(
                        colbillId = colbillId,
                        sent = true,
                        message = "전송 완료"
                    )
                }
            } catch (e: Exception) {
                SendCollectionResult(
                    colbillId = colbillId,
                    sent = false,
                    message = e.message ?: "전송 실패"
                )
            }
        }

        return SendCollectionResponse(
            sentCount = results.count { it.sent },
            results = results
        )
    }
}
