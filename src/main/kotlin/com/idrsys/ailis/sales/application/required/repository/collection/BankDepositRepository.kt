package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankDepositBatchCommand
import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.domain.model.BankDeposit
import kotlinx.coroutines.flow.Flow

/**
 * Bank Deposit Repository (Port Interface)
 *
 * Combines basic CRUD operations and complex queries
 */
interface BankDepositRepository {
    // Basic CRUD operations
    suspend fun save(bankDeposit: BankDeposit): BankDeposit
    suspend fun findById(id: String): BankDeposit?
    suspend fun delete(bankDeposit: BankDeposit)

    // Custom query operations
    /**
     * Find bank deposits
     */
    fun findBankDeposits(searchParam: BankDepositSearchParam): Flow<BankDeposit>

    /**
     * Find bank deposit by ID
     */
    suspend fun findBankDepositById(bankDepositId: String): BankDeposit?

    /**
     * Update regYn flag
     */
    suspend fun updateRegYn(bankDepositId: String, regYn: Boolean, updater: String)

    /**
     * 배치 저장 — 가수금전표번호(surecpSlstmtNo) 기준 중복 제외 후 신규 건만 INSERT
     *
     * @return 실제 저장된 건수
     */
    suspend fun batchInsert(commands: List<BankDepositBatchCommand>, creator: String): Int
}
