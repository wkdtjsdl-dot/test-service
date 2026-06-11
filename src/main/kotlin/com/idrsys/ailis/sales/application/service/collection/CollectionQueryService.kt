package com.idrsys.ailis.sales.application.service.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.dto.response.BankDepositResponse
import com.idrsys.ailis.sales.application.dto.response.CardPaymentResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionBillListResponse
import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CardPaymentRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionBillRepository
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
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
    private val collectionBillRepository: CollectionBillRepository,
    private val cardPaymentRepository: CardPaymentRepository,
    private val bankDepositRepository: BankDepositRepository,
    private val custCustomRepository: CustCustomRepository,
    private val baseServicePort: BaseServicePort,
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
        val transactions = collectionLedgerRepository.findLedgerTransactionsWithBalance(custCd).toList()
        val cust = custCustomRepository.findByCustCd(custCd)

        // 각 트랜잭션의 colbillAmt를 구분에 따라 합산
        val totalDemandAmt = transactions
            .filter { it.division == "청구" }
            .fold(java.math.BigDecimal.ZERO) { acc, t -> acc.add(t.colbillAmt) }
        val totalCollectionAmt = transactions
            .filter { it.division == "수금" }
            .fold(java.math.BigDecimal.ZERO) { acc, t -> acc.add(t.colbillAmt) }
        val arBalance = transactions.lastOrNull()?.balance ?: java.math.BigDecimal.ZERO

        return CollectionLedgerResponse(
            custCd = custCd,
            custNm = null, // TODO: Fetch from customer service
            bizrno = cust?.bizrno,
            transactions = transactions,
            totalDemandAmt = totalDemandAmt,
            totalCollectionAmt = totalCollectionAmt,
            arBalance = arBalance
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
    override suspend fun getBankDepositList(
        searchParam: BankDepositSearchParam
    ): Flow<BankDepositResponse> {
        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("CRCY")) ?: emptyMap()
        val crcyNmByCd = systemCodes["CRCY"]?.associate { it.cd to it.cdNm } ?: emptyMap()
        return bankDepositRepository.findBankDeposits(searchParam)
            .map { BankDepositResponse.from(it).copy(crcyNm = crcyNmByCd[it.crcyCd]) }
    }

    /**
     * Get collection bills generated from a card payment
     */
    override suspend fun getColbillsByCardPayId(cardPayId: String): Flow<CollectionBillListResponse> {
        val payMethodNmByCd = resolvePayMethodNames()
        return collectionBillRepository.findByCardPayId(cardPayId)
            .map { bill -> bill.copy(payMethodNm = payMethodNmByCd[bill.payMethodCd]) }
    }

    /**
     * Get collection bills generated from a bank deposit
     */
    override suspend fun getColbillsByBankDepositId(bankDepositId: String): Flow<CollectionBillListResponse> {
        val payMethodNmByCd = resolvePayMethodNames()
        return collectionBillRepository.findByBankDepositId(bankDepositId)
            .map { bill -> bill.copy(payMethodNm = payMethodNmByCd[bill.payMethodCd]) }
    }

    private suspend fun resolvePayMethodNames(): Map<String, String> {
        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("PMMT")) ?: emptyMap()
        return systemCodes["PMMT"]?.associate { it.cd to it.cdNm } ?: emptyMap()
    }
}
