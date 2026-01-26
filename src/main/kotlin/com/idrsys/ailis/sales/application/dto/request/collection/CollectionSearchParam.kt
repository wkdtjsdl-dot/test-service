package com.idrsys.ailis.sales.application.dto.request.collection

import java.time.LocalDate

/**
 * Collection Ledger Search Parameters
 */
data class CollectionLedgerSearchParam(
    val custCd: String,
    val startDt: LocalDate,
    val endDt: LocalDate
)

/**
 * Card Payment Search Parameters
 */
data class CardPaymentSearchParam(
    val startDt: String,
    val endDt: String,
    val payDivCd: String? = null,  // "10": approved, "20": cancelled
    val regYn: Boolean? = null
)

/**
 * Bank Deposit Search Parameters
 */
data class BankDepositSearchParam(
    val startDt: LocalDate,
    val endDt: LocalDate,
    val regYn: Boolean? = null
)
