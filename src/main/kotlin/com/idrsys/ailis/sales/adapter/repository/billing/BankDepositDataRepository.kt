package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.BankDeposit
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * BankDepositDataRepository
 *
 * R2DBC reactive repository for BankDeposit entity
 */
@Repository
interface BankDepositDataRepository : CoroutineCrudRepository<BankDeposit, String> {

    /**
     * Find bank deposits by registration status
     */
    fun findByRegYn(regYn: Boolean): Flow<BankDeposit>

    /**
     * Find bank deposits by registration status and deposit date range
     */
    fun findByRegYnAndDepositDtBetween(
        regYn: Boolean,
        startDate: String,
        endDate: String
    ): Flow<BankDeposit>

    /**
     * Find bank deposits by account number
     */
    fun findByAccountNo(accountNo: String): Flow<BankDeposit>

    /**
     * Find bank deposits by account year
     */
    fun findByAccountYear(accountYear: String): Flow<BankDeposit>
}
