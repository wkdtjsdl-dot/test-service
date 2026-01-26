package com.idrsys.ailis.sales.application.dto.request.collection

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Register Collection Command
 *
 * Command to register payment (card or bank deposit)
 */
data class RegisterCollectionCommand(
    val custCd: String,
    val colbillDt: LocalDate,
    val payMethodCd: String,
    val payAmt: BigDecimal,
    val cardPayId: String? = null,
    val cardApprNo: String? = null,
    val cardNo: String? = null,
    val cardBillNo: String? = null,
    val bankDepositId: String? = null,
    val accountYear: String? = null,
    val surecpSlstmtNo: String? = null,
    val advreceYn: Boolean = false,
    val remark: String? = null
)

/**
 * Register Split Payment Command
 *
 * Command to split one payment across multiple customers
 */
data class RegisterSplitPaymentCommand(
    val cardPayId: String? = null,
    val bankDepositId: String? = null,
    val splits: List<SplitPaymentInfo>
)

data class SplitPaymentInfo(
    val custCd: String,
    val payAmt: BigDecimal,
    val colbillDt: LocalDate
)

/**
 * Send Collection to ERP Command
 */
data class SendCollectionToErpCommand(
    val colbillIds: List<String>
)
