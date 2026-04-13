package com.idrsys.ailis.sales.adapter.external.sap

import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.request.billing.SapInvcPostingRow
import com.idrsys.ailis.sales.application.dto.request.ifre010.SapIfRe010Row
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult
import com.idrsys.ailis.sales.application.dto.response.sap.SapIfRe010Result
import com.idrsys.ailis.sales.application.dto.response.sap.SapInvcPostingResult
import com.idrsys.ailis.sales.application.required.sap.BankDepositErpPort
import com.idrsys.ailis.sales.application.required.sap.CollectionErpPort
import com.idrsys.ailis.sales.application.required.sap.InvoiceErpPort
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!sap")
class NoOpSapAdapter : CollectionErpPort, InvoiceErpPort, BankDepositErpPort {

    override fun sendCollection(rtype: String, row: SapIfRe010Row): SapIfRe010Result {
        throw UnsupportedOperationException("SAP is not configured. Enable the 'sap' profile to use this feature.")
    }

    override fun postInvoices(rows: List<SapInvcPostingRow>): List<SapInvcPostingResult> {
        throw UnsupportedOperationException("SAP is not configured. Enable the 'sap' profile to use this feature.")
    }

    override fun fetchBankDeposits(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult> {
        throw UnsupportedOperationException("SAP is not configured. Enable the 'sap' profile to use this feature.")
    }
}
