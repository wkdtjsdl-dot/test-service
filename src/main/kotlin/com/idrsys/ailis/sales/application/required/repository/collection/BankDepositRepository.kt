package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.domain.model.BankDeposit
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

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
     * Find bank deposits with pagination
     */
    fun findBankDeposits(searchParam: BankDepositSearchParam, pageable: Pageable): Flow<BankDeposit>

    /**
     * Count bank deposits
     */
    suspend fun countBankDeposits(searchParam: BankDepositSearchParam): Long

    /**
     * Find bank deposit by ID
     */
    suspend fun findBankDepositById(bankDepositId: String): BankDeposit?

    /**
     * Update regYn flag
     */
    suspend fun updateRegYn(bankDepositId: String, regYn: Boolean, updater: String)
}
