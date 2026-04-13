package com.idrsys.ailis.sales.application.required.sap

import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult

interface BankDepositErpPort {
    fun fetchBankDeposits(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult>
}
