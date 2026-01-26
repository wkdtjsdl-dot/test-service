package com.idrsys.ailis.sales.application.service.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.response.BankDepositResponse
import com.idrsys.ailis.sales.application.dto.response.CardPaymentResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerTransaction
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CardPaymentRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.application.usecase.collection.CollectionQueryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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
    override suspend fun getCollectionLedger(custCd: String): CollectionLedgerResponse {
        // Get all ledger entries for customer in period
        val ledgers = collectionLedgerRepository.findByCustCdOrderByColbillDtAsc(
            custCd,
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
            custCd = custCd,
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
    override fun getCardPaymentList(
        searchParam: CardPaymentSearchParam
    ): Flow<CardPaymentResponse> {
        return cardPaymentRepository.findCardPayments(searchParam)
            .map { CardPaymentResponse.from(it) }
    }

    /**
     * Get bank deposit list (unregistered or all)
     */
    override fun getBankDepositList(
        searchParam: BankDepositSearchParam
    ): Flow<BankDepositResponse> {
        return bankDepositRepository.findBankDeposits(searchParam)
            .map { BankDepositResponse.from(it) }
    }
}
