package com.idrsys.ailis.sales.application.dto.request.bankdeposit

import java.math.BigDecimal
import java.time.LocalDate

data class BankDepositBatchCommand(
    val accountDivCd: String?,
    val accountNo: String?,
    val accountYear: String?,
    val surecpSlstmtNo: String?,
    val depositDt: LocalDate,
    val depositAmt: BigDecimal,
    val outamt: BigDecimal?,
    val crcyCd: String?,
    val remark: String?
)
