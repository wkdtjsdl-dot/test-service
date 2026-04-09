package com.idrsys.ailis.sales.application.required.sap

import com.idrsys.ailis.sales.application.dto.request.billing.SapInvcPostingRow
import com.idrsys.ailis.sales.application.dto.response.sap.SapInvcPostingResult

interface InvoiceErpPort {
    fun postInvoices(rows: List<SapInvcPostingRow>): List<SapInvcPostingResult>
}
