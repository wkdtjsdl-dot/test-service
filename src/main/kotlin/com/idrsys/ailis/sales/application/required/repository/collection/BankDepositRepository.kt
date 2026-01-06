package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.BankDeposit
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Bank Deposit Repository (Data Interface)
 */
interface BankDepositRepository : CoroutineCrudRepository<BankDeposit, String>
