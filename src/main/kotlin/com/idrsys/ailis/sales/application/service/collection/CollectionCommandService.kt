package com.idrsys.ailis.sales.application.service.collection

import io.github.oshai.kotlinlogging.KotlinLogging
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionListSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterCollectionCommand
import com.idrsys.ailis.sales.application.dto.request.collection.RegisterSplitPaymentCommand
import com.idrsys.ailis.sales.application.dto.request.collection.SendCollectionToErpCommand
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateClosingRequest
import com.idrsys.ailis.sales.application.dto.request.collection.UpdateCollectionCommand
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.DeleteCollectionBillResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResponse
import com.idrsys.ailis.sales.application.dto.response.SendCollectionResult
import com.idrsys.ailis.sales.application.dto.response.SplitCollectionResponse
import com.idrsys.ailis.sales.application.dto.request.ifre010.SapIfRe010Row
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.repository.collection.*
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.sap.CollectionErpPort
import com.idrsys.ailis.sales.application.usecase.collection.CollectionCommandUseCase
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.ailis.sales.domain.model.CollectionBillHst
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import com.idrsys.ailis.sales.domain.model.CollectionLedgerHst
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private val collectionLedgerHstRepository: CollectionLedgerHstRepository,
    private val collectionBillHstRepository: CollectionBillHstRepository,
    private val cardPaymentRepository: CardPaymentRepository,
    private val bankDepositRepository: BankDepositRepository,
    private val custCustomRepository: CustCustomRepository,
    private val collectionErpPort: CollectionErpPort,
    private val baseServicePort: BaseServicePort,
) : CollectionCommandUseCase {

    private val logger = KotlinLogging.logger {}

    override suspend fun findCollectionBills(searchParam: CollectionListSearchParam): Flow<CollectionBillListResponse> {
        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("CRCY")) ?: emptyMap()
        val crcyNameByCd = systemCodes["CRCY"]?.associate { it.cd to it.cdNm } ?: emptyMap()
        return collectionBillRepository.findCollectionBills(searchParam)
            .map { bill -> bill.copy(crcyCd = crcyNameByCd[bill.crcyCd] ?: bill.crcyCd) }
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


        // 2. Create collection ledger FIRST
        val ledger = CollectionLedger.createForCollection(
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            colbillItemNm = "결제(카드)",
            colbillItemDtl = command.cardCompNm,
            payAmt = command.payAmt,
            creator = adminId
        )
        val savedLedger = collectionLedgerRepository.save(ledger)

        // 3. Create collection bill with ledger ID
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
            instlMonth = command.instlMonth,
            bankDepositId = null,
            accountYear = null,
            surecpSlstmtNo = null,
            advreceYn = command.advreceYn,
            closingCd = command.closingCd,
            colledgerId = savedLedger.colledgerId,
            cardCompCd = command.cardCompCd,
            cardCompNm = command.cardCompNm,
            sendYn = false,
            remark = command.remark,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsNew()
        }

        val savedBill = collectionBillRepository.save(collectionBill)

        val now = LocalDateTime.now()
        collectionLedgerHstRepository.save(CollectionLedgerHst.of(savedLedger, "HST_C", "수금 등록", adminId, now))
        collectionBillHstRepository.save(CollectionBillHst.of(savedBill, "HST_C", "수금 등록", adminId, now))

        // 4. 카드 미정산 금액 및 등록여부 계산
        val remainAmt = cardPayment.outamt.subtract(command.payAmt)

        if (remainAmt < BigDecimal.ZERO) {
            throw UserDefinedException(
                "INVALID_PAYMENT_AMOUNT",
                "요청 금액이 미정산 금액을 초과합니다"
            )
        }

        val updatedCardPayment = cardPayment.copy(
            outamt = remainAmt,
            regYn = remainAmt.compareTo(BigDecimal.ZERO) == 0
        )

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

        // 2. Create collection ledger FIRST
        val ledger = CollectionLedger.createForCollection(
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            colbillItemNm = "결제(은행)",
            colbillItemDtl = command.cardCompNm,
            payAmt = command.payAmt,
            creator = adminId
        )
        val savedLedger = collectionLedgerRepository.save(ledger)

        // 3. Create collection bill with ledger ID
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
            instlMonth = null,
            bankDepositId = command.bankDepositId,
            accountYear = command.accountYear,
            surecpSlstmtNo = command.surecpSlstmtNo,
            advreceYn = command.advreceYn,
            closingCd = command.closingCd,
            colledgerId = savedLedger.colledgerId,
            cardCompCd = command.cardCompCd,
            cardCompNm = command.cardCompNm,
            sendYn = false,
            remark = command.remark,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsNew()
        }

        val savedBill = collectionBillRepository.save(collectionBill)

        val now = LocalDateTime.now()
        collectionLedgerHstRepository.save(CollectionLedgerHst.of(savedLedger, "HST_C", "수금 등록", adminId, now))
        collectionBillHstRepository.save(CollectionBillHst.of(savedBill, "HST_C", "수금 등록", adminId, now))

        // 4. 은행 미정산 금액 및 등록여부 계산
        val remainAmt = bankDeposit.outamt?.subtract(command.payAmt)

        if (remainAmt != null && remainAmt < BigDecimal.ZERO) {
          throw UserDefinedException(
            "INVALID_PAYMENT_AMOUNT",
            "요청 금액이 미정산 금액을 초과합니다"
          )
        }

        val updatedBankDeposit = bankDeposit.copy(
            outamt = remainAmt,
            regYn = remainAmt?.compareTo(BigDecimal.ZERO) == 0
        )

        bankDepositRepository.save(updatedBankDeposit)

        // 5. Return response
        return CollectionBillResponse.from(savedBill, savedLedger.colledgerId)
    }

    override suspend fun updateCardPayment(
        colbillId: String,
        request: UpdateCollectionCommand,
        adminId: String
    ): CollectionBillResponse {
        val colBill = collectionBillRepository.findById(colbillId)
            ?: throw UserDefinedException("COLBILL NOT FOUND", "입금 내역을 찾을 수 없습니다.")

        if (request.cardPayId.isNullOrEmpty() || request.cardPayId == "") {
            throw UserDefinedException("CARDPAYID IS NULL OR EMPTY", "카드 아이디가 null입니다.")
        }

        val cardPayment = request.cardPayId.let { cardPaymentRepository.findById(it) }
            ?: throw UserDefinedException("CARD_PAYMENT_NOT_FOUND", "카드 결제를 찾을 수 없습니다: ${request.cardPayId}")

        if (colBill.closingCd == "CLCD_Y") {
            throw UserDefinedException("COLLECTION IS CLOSED", "마감된 상태입니다.")
        }

        // DTO의 값으로 업데이트 (DTO에 있는 필드만)
        val updatedColBill = CollectionBill(
            colbillId = colBill.colbillId,
            custCd = request.custCd,
            colbillDt = request.colbillDt,
            payMethodCd = request.payMethodCd,
            payAmt = request.payAmt,
            cardPayId = request.cardPayId,
            cardApprNo = request.cardApprNo,
            cardNo = request.cardNo,
            instlMonth = request.instlMonth,
            cardBillNo = request.cardBillNo,
            bankDepositId = null,
            accountYear = null,
            surecpSlstmtNo = null,
            advreceYn = request.advreceYn,
            closingCd = request.closingCd,
            remark = request.remark,
            cardCompCd = request.cardCompCd,
            cardCompNm = request.cardCompNm,
            // DTO에 없는 필드는 기존값 유지
            salesSlstmtNo = colBill.salesSlstmtNo,
            colledgerId = colBill.colledgerId,
            sendYn = colBill.sendYn,
            creator = colBill.creator,
            createDtime = colBill.createDtime,
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsExisting()
        }

        val saved = collectionBillRepository.save(updatedColBill)
        val now = LocalDateTime.now()
        collectionBillHstRepository.save(CollectionBillHst.of(saved, "HST_M", "수금 수정(카드)", adminId, now))

        // CollectionLedger 업데이트 (colledgerId가 있으면)
        colBill.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
                ?: throw UserDefinedException("LEDGER_NOT_FOUND", "원장을 찾을 수 없습니다.")

            ledger.updateCollection(
                colbillDt = request.colbillDt,
                colbillAmt = request.payAmt,
                colbillItemNm = "결제(카드)",
                colbillItemDtl = request.cardCompNm,
                updater = adminId
            )

            collectionLedgerRepository.save(ledger)
            collectionLedgerHstRepository.save(CollectionLedgerHst.of(ledger, "HST_M", "수금 수정(카드)", adminId, now))
        }

        // 카드 미정산 금액 및 등록여부 계산
        val diff = request.payAmt.subtract(colBill.payAmt)
        val remainAmt = cardPayment.outamt.subtract(diff)

        if (remainAmt < BigDecimal.ZERO) {
            throw UserDefinedException(
                "INVALID_PAYMENT_AMOUNT",
                "요청 금액이 미정산 금액을 초과합니다"
            )
        }

        val updatedCardPayment = cardPayment.copy(
            outamt = remainAmt,
            regYn = remainAmt.compareTo(BigDecimal.ZERO) == 0
        )

        cardPaymentRepository.save(updatedCardPayment)

        return CollectionBillResponse.from(saved)
    }

    override suspend fun updateBankDeposit(colbillId:String, request: UpdateCollectionCommand, adminId: String): CollectionBillResponse {
        val colBill = collectionBillRepository.findById(colbillId) ?: throw UserDefinedException("COLBILL NOT FOUND","입금 내역을 찾을 수 없습니다.")
        if(request.bankDepositId.isNullOrEmpty() || request.bankDepositId == "" ) {
            throw UserDefinedException("BANKDEPOSITID IS NULL OR EMPTY","은행 아이디가 null입니다.")
        }
        val bankDeposit = bankDepositRepository.findById(request.bankDepositId)
            ?: throw UserDefinedException("BANK_DEPOSIT_NOT_FOUND", "은행 입금을 찾을 수 없습니다: ${request.bankDepositId}")

        if (colBill.closingCd === "CLCD_Y") {
            throw UserDefinedException("COLLECTION IS CLOSED","마감된 상태입니다.")
        }
        // DTO의 값으로 업데이트 (DTO에 있는 필드만)
        val updatedColBill = CollectionBill(
            colbillId = colBill.colbillId,
            custCd = request.custCd,
            colbillDt = request.colbillDt,
            payMethodCd = request.payMethodCd,
            payAmt = request.payAmt,
            cardPayId = null,
            cardApprNo = null,
            cardNo = null,
            cardBillNo = null,
            bankDepositId = request.bankDepositId,
            accountYear = request.accountYear,
            surecpSlstmtNo = request.surecpSlstmtNo,
            advreceYn = request.advreceYn,
            closingCd = request.closingCd,
            remark = request.remark,
            // DTO에 없는 필드는 기존값 유지
            cardCompCd = request.cardCompCd,
            cardCompNm = request.cardCompNm,
            instlMonth = colBill.instlMonth,
            salesSlstmtNo = colBill.salesSlstmtNo,
            colledgerId = colBill.colledgerId,
            sendYn = colBill.sendYn,
            creator = colBill.creator,
            createDtime = colBill.createDtime,
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsExisting()
        }

        val saved = collectionBillRepository.save(updatedColBill)
        val now = LocalDateTime.now()
        collectionBillHstRepository.save(CollectionBillHst.of(saved, "HST_M", "수금 수정(은행)", adminId, now))

        // CollectionLedger 업데이트 (colledgerId가 있으면)
        colBill.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
                ?: throw UserDefinedException("LEDGER_NOT_FOUND", "원장을 찾을 수 없습니다.")

            ledger.updateCollection(
                colbillDt = request.colbillDt,
                colbillAmt = request.payAmt,
                colbillItemNm = "결제(은행)",
                colbillItemDtl = request.cardCompNm,
                updater = adminId
            )

            collectionLedgerRepository.save(ledger)
            collectionLedgerHstRepository.save(CollectionLedgerHst.of(ledger, "HST_M", "수금 수정(은행)", adminId, now))
        }

        // 은행 미정산 금액 및 등록여부 계산
        val diff = request.payAmt.subtract(colBill.payAmt)
        val remainAmt = bankDeposit.outamt?.subtract(diff)

        if (remainAmt != null && remainAmt < BigDecimal.ZERO) {
          throw UserDefinedException(
            "INVALID_PAYMENT_AMOUNT",
            "요청 금액이 미정산 금액을 초과합니다"
          )
        }

        val updatedBankPayment = bankDeposit.copy(
            outamt = remainAmt,
            regYn = remainAmt?.compareTo(BigDecimal.ZERO) == 0
        )

        bankDepositRepository.save(updatedBankPayment)
        
        return CollectionBillResponse.from(saved)
    }

    // CollectionService.kt
    override suspend fun setColbillClosing(
        colbillId: String,
        request: UpdateClosingRequest,
        adminId: String
    ): CollectionBillResponse {
        val colBill = collectionBillRepository.findById(colbillId)
            ?: throw UserDefinedException("COLBILL NOT FOUND", "입금 내역을 찾을 수 없습니다.")

        colBill.updateClosingStatus(request.closingCd, adminId)
        colBill.setAsExisting()

        val saved = collectionBillRepository.save(colBill)
        collectionBillHstRepository.save(CollectionBillHst.of(saved, "HST_M", "수금 마감 처리", adminId, LocalDateTime.now()))
        return CollectionBillResponse.from(saved)
    }

    override suspend fun registerCashOrBillPayment(
        command: RegisterCollectionCommand,
        adminId: String
    ): CollectionBillResponse {

        // 결제 방법에 따른 원장 항목명 결정
        val (colbillItemNm, isValidPayMethod) = when (command.payMethodCd) {
            "PMMT_CS" -> "결제(현금)" to true
            "PMMT_BL" -> "결제(어음)" to true
            else -> "" to false
        }

        require(isValidPayMethod) {
            "지원하지 않는 결제 방법입니다: ${command.payMethodCd}"
        }

        val ledger = CollectionLedger.createForCollection(
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            colbillItemNm = colbillItemNm,
            colbillItemDtl = command.cardCompNm,
            payAmt = command.payAmt,
            creator = adminId
        )

        val savedLedger = collectionLedgerRepository.save(ledger)

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
            instlMonth = null,
            bankDepositId = null,
            accountYear = null,
            surecpSlstmtNo = null,
            advreceYn = command.advreceYn,
            closingCd = command.closingCd,
            colledgerId = savedLedger.colledgerId,
            cardCompCd = null,
            cardCompNm = null,
            sendYn = false,
            remark = command.remark,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsNew()
        }

        val savedBill = collectionBillRepository.save(collectionBill)

        val now = LocalDateTime.now()
        collectionLedgerHstRepository.save(CollectionLedgerHst.of(savedLedger, "HST_C", "수금 등록", adminId, now))
        collectionBillHstRepository.save(CollectionBillHst.of(savedBill, "HST_C", "수금 등록", adminId, now))

        return CollectionBillResponse.from(savedBill, savedLedger.colledgerId)
    }

    override suspend fun updateCashOrBillPayment(
        colbillId: String,
        command: UpdateCollectionCommand,
        adminId: String
    ): CollectionBillResponse {
        val colBill = collectionBillRepository.findById(colbillId) ?: throw UserDefinedException("COLBILL NOT FOUND","입금 내역을 찾을 수 없습니다.")
        if (colBill.closingCd === "CLCD_Y") {
            throw UserDefinedException("COLLECTION IS CLOSED","마감된 상태입니다.")
        }
        // 결제 방법에 따른 원장 항목명 결정
        val (colbillItemNm) = when (command.payMethodCd) {
            "PMMT_CS" -> "결제(현금)" to true
            "PMMT_BL" -> "결제(어음)" to true
            else -> "" to false
        }

        // DTO의 값으로 업데이트 (DTO에 있는 필드만)
        val updatedColBill = CollectionBill(
            colbillId = colBill.colbillId,
            custCd = command.custCd,
            colbillDt = command.colbillDt,
            payMethodCd = command.payMethodCd,
            payAmt = command.payAmt,
            cardPayId = null,
            cardApprNo = null,
            cardNo = null,
            cardBillNo = null,
            bankDepositId = null,
            accountYear = null,
            surecpSlstmtNo = null,
            advreceYn = command.advreceYn,
            closingCd = command.closingCd,
            remark = command.remark,
            // DTO에 없는 필드는 기존값 유지
            cardCompCd = null,
            cardCompNm = null,
            instlMonth = colBill.instlMonth,
            salesSlstmtNo = colBill.salesSlstmtNo,
            colledgerId = colBill.colledgerId,
            sendYn = colBill.sendYn,
            creator = colBill.creator,
            createDtime = colBill.createDtime,
            updater = adminId,
            updateDtime = LocalDateTime.now()
        ).apply {
            setAsExisting()
        }
        val saved = collectionBillRepository.save(updatedColBill)
        val now = LocalDateTime.now()
        collectionBillHstRepository.save(CollectionBillHst.of(saved, "HST_M", "수금 수정", adminId, now))

        // CollectionLedger 업데이트 (colledgerId가 있으면)
        colBill.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
                ?: throw UserDefinedException("LEDGER_NOT_FOUND", "원장을 찾을 수 없습니다.")

            ledger.updateCollection(
                colbillDt = command.colbillDt,
                colbillAmt = command.payAmt,
                colbillItemNm = colbillItemNm,
                colbillItemDtl = command.cardCompNm,
                updater = adminId
            )

            collectionLedgerRepository.save(ledger)
            collectionLedgerHstRepository.save(CollectionLedgerHst.of(ledger, "HST_M", "수금 수정", adminId, now))
        }

        return CollectionBillResponse.from(saved)

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
                colbillItemDtl = "",
                payAmt = split.payAmt,
                creator = adminId
            )

            val savedLedger = collectionLedgerRepository.save(ledger)
            val now = LocalDateTime.now()
            collectionLedgerHstRepository.save(CollectionLedgerHst.of(savedLedger, "HST_C", "수금 등록(분할)", adminId, now))
            collectionBillHstRepository.save(CollectionBillHst.of(savedBill, "HST_C", "수금 등록(분할)", adminId, now))

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

        val now = LocalDateTime.now()

        // 3. Delete associated ledger using colledgerId (save history first)
        collectionBill.colledgerId?.let { colledgerId ->
            val ledger = collectionLedgerRepository.findById(colledgerId)
            if (ledger != null) {
                collectionLedgerHstRepository.save(CollectionLedgerHst.of(ledger, "HST_D", "수금 삭제", adminId, now))
                collectionLedgerRepository.delete(ledger)
            }
        }

        // 4. 카드/은행 미정산 금액 및 등록여부 계산
        if (collectionBill.cardPayId != null) {
            val cardPayment = cardPaymentRepository.findById(collectionBill.cardPayId.toString())

            val newOutAmt = cardPayment?.outamt?.add(collectionBill.payAmt)

            val updatedCardPayment = newOutAmt?.let {
                cardPayment.copy(
                    outamt = it,
                    regYn = false
                )
            }

            if (cardPayment != null) {
                updatedCardPayment?.let { cardPaymentRepository.save(it) }
            }
        } else if (collectionBill.bankDepositId != null) {
            val bankDeposit = bankDepositRepository.findById(collectionBill.bankDepositId.toString())

            val newOutAmt = bankDeposit?.outamt?.add(collectionBill.payAmt)

            val updatedBankPayment = newOutAmt?.let {
                bankDeposit.copy(
                    outamt = it,
                    regYn = false
                )
            }

            if (bankDeposit != null) {
                updatedBankPayment?.let { bankDepositRepository.save(it) }
            }
        }

        // 5. Delete collection bill (save history first)
        collectionBillHstRepository.save(CollectionBillHst.of(collectionBill, "HST_D", "수금 삭제", adminId, now))
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
                    // 수금유형(rtype) 결정: 은행=5N/5D, 카드=6N/6D (N=일반, D=선수금)
                    val rtype = when {
                        bill.payMethodCd == "PMMT_DP" && !bill.advreceYn -> "5N"
                        bill.payMethodCd == "PMMT_DP" &&  bill.advreceYn -> "5D"
                        bill.payMethodCd == "PMMT_CD" && !bill.advreceYn -> "6N"
                        bill.payMethodCd == "PMMT_CD" &&  bill.advreceYn -> "6D"
                        else -> throw UserDefinedException(
                            "UNSUPPORTED_PAY_METHOD",
                            "ERP 전송 미지원 결제수단: ${bill.payMethodCd}"
                        )
                    }

                    val cust = custCustomRepository.findByCustCd(bill.custCd)
                    val dateStr = bill.colbillDt.format(DateTimeFormatter.BASIC_ISO_DATE)
                    val isBank = bill.payMethodCd == "PMMT_DP"
                    val bankDeposit = if (isBank) bill.bankDepositId?.let { bankDepositRepository.findById(it) } else null
                    val cardPayment = if (!isBank) bill.cardPayId?.let { cardPaymentRepository.findById(it) } else null
                    logger.info("bill fields | colbillId={} surecpSlstmtNo={} bankDepositId={} bankDeposit.surecpSlstmtNo={}",
                        colbillId, bill.surecpSlstmtNo, bill.bankDepositId, bankDeposit?.surecpSlstmtNo)

                    val erpSeqNo = collectionBillRepository.nextErpSeqNo()

                    val row: SapIfRe010Row = if (isBank) {
                        SapIfRe010Row.Bank(
                            budat     = dateStr,
                            kkber     = "3300",
                            vkgrp     = "901",
                            vkbur     = "3312",
                            seq       = erpSeqNo,
                            gjahr     = bill.colbillDt.year.toString(),
                            monat     = bill.colbillDt.monthValue.toString().padStart(2, '0'),
                            uzawe     = "02",
                            wrbtr     = bill.payAmt,
                            inDate    = dateStr,
                            stcd2     = cust?.bizrno,
                            sgtxt     = "현금",
                            vkorg     = "3310",
                            gsber     = "3300",
                            bankl     = bankDeposit?.accountDivCd ?: command.bankl,
                            bankn     = bankDeposit?.accountNo ?: command.bankn,
                            belnrGasu = bill.surecpSlstmtNo,
                            kunnrzz   = null,
                        )
                    } else {
                        val cardPayDt = cardPayment?.payDt ?: dateStr
                        SapIfRe010Row.Card(
                            budat     = cardPayDt,
                            kkber     = "3300",
                            vkgrp     = "901",
                            vkbur     = "3312",
                            seq       = erpSeqNo,
                            gjahr     = bill.colbillDt.year.toString(),
                            monat     = bill.colbillDt.monthValue.toString().padStart(2, '0'),
                            uzawe     = "04",
                            wrbtr     = bill.payAmt,
                            inDate    = cardPayDt,
                            stcd2     = cust?.bizrno,
                            sgtxt     = "카드",
                            vkorg     = "3310",
                            gsber     = "3300",
                            kunnrzz   = null,
                            zfbdt     = cardPayDt,
                            zstmemb   = null,
                            zcompcd   = bill.cardCompCd,
                            appramt   = bill.payAmt,
                            insomonth = bill.instlMonth,
                            rudat     = cardPayDt,
                        )
                    }

                    val erpResult = collectionErpPort.sendCollection(rtype, row)
                    logger.info("ZFI_IF_RE_010 result | colbillId={} rtype={} returnCode={} returnMessage={} zstatus={} zresult={} belnr1={} belnr2={}",
                        colbillId, rtype,
                        erpResult.returnCode, erpResult.returnMessage,
                        erpResult.zstatus, erpResult.zresult,
                        erpResult.belnr1, erpResult.belnr2
                    )

                    if (erpResult.zstatus == "E" || erpResult.returnCode == "E") {
                        SendCollectionResult(
                            colbillId = colbillId,
                            sent = false,
                            message = erpResult.zresult ?: erpResult.returnMessage ?: "ERP 전송 실패"
                        )
                    } else {
                        bill.markAsSent(adminId, erpResult.belnr1)
                        collectionBillRepository.save(bill)
                        SendCollectionResult(
                            colbillId = colbillId,
                            sent = true,
                            message = erpResult.returnMessage ?: "전송 완료"
                        )
                    }
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
