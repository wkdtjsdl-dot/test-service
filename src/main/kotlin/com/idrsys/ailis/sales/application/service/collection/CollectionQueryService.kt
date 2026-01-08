package com.idrsys.ailis.sales.application.service.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CollectionSearchParam
import com.idrsys.ailis.sales.application.dto.response.BankDepositResponse
import com.idrsys.ailis.sales.application.dto.response.CardPaymentResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerTransaction
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CardPaymentRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.application.usecase.collection.CollectionQueryUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Collection Query Service
 *
 * Implements read operations for collection domain
 */
@Service
@Transactional(readOnly = true)
class CollectionQueryService(
    private val collectionLedgerRepository: CollectionLedgerRepository,
    private val cardPaymentRepository: CardPaymentRepository,
    private val bankDepositRepository: BankDepositRepository,
) : CollectionQueryUseCase {

    /**
     * Get collection ledger for customer
     *
     * Business Rules:
     * 1. Transactions sorted by date ascending
     * 2. Balance calculated cumulatively
     * 3. AR balance = Total demand - Total collection
     */
    override suspend fun getCollectionLedger(searchParam: CollectionSearchParam): CollectionLedgerResponse {
        // Get all ledger entries for customer in period
        val ledgers = collectionLedgerRepository.findByCustCdAndColbillDtBetweenOrderByColbillDtAsc(
            searchParam.custCd!!,
            searchParam.startDt,
            searchParam.endDt
        ).toList()

        // Calculate running balance and totals
        var runningBalance = java.math.BigDecimal.ZERO
        var totalDemandAmt = java.math.BigDecimal.ZERO
        var totalCollectionAmt = java.math.BigDecimal.ZERO

        val transactions = ledgers.map { ledger ->
            val amount = ledger.colbillAmt
            if (ledger.colbillDivCd == "0") {
                // Demand (increase AR)
                runningBalance = runningBalance.add(amount)
                totalDemandAmt = totalDemandAmt.add(amount)
            } else {
                // Collection (decrease AR)
                runningBalance = runningBalance.subtract(amount)
                totalCollectionAmt = totalCollectionAmt.add(amount)
            }

            CollectionLedgerTransaction.from(ledger, runningBalance)
        }

        return CollectionLedgerResponse(
            custCd = searchParam.custCd,
            custNm = null, // TODO: Fetch from customer service
            transactions = transactions,
            totalDemandAmt = totalDemandAmt,
            totalCollectionAmt = totalCollectionAmt,
            arBalance = runningBalance
        )
    }

    /**
     * Get card payment list (unregistered or all)
     */
    override suspend fun getCardPaymentList(
        searchParam: CardPaymentSearchParam,
        pageable: Pageable
    ): Page<CardPaymentResponse> {
        val total = cardPaymentRepository.countCardPayments(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val cardPayments = cardPaymentRepository.findCardPayments(searchParam, pageable)
            .map { CardPaymentResponse.from(it) }
            .toList()
        return PageImpl(cardPayments, pageable, total)
    }

    /**
     * Get bank deposit list (unregistered or all)
     */
    override suspend fun getBankDepositList(
        searchParam: BankDepositSearchParam,
        pageable: Pageable
    ): Page<BankDepositResponse> {
        val total = bankDepositRepository.countBankDeposits(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val bankDeposits = bankDepositRepository.findBankDeposits(searchParam, pageable)
            .map { BankDepositResponse.from(it) }
            .toList()
        return PageImpl(bankDeposits, pageable, total)
    }
}
