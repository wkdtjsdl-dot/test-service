package com.idrsys.ailis.sales.application.dto.response.sap

import java.math.BigDecimal

/**
 * SAP RFC ZFI_IF_RE_020 출력 테이블 IT_713 Row
 *
 * SAP 필드 매핑:
 *   BANKL  → bankl        (은행번호)
 *   BANKN  → bankn        (계좌번호)
 *   GJAHR  → accountYear  (회계연도)
 *   BELNR  → surecpSlstmtNo (가수금 전표번호)
 *   BUDAT  → depositDt    (입금일, yyyyMMdd)
 *   WRBTR  → depositAmt   (입금액)
 *   AVLAMT → outamt       (미정산금액)
 *   WAERS  → crcyCd       (통화)
 *   SGTXT  → remark       (적요)
 */
data class SapBankDepositResult(
    val bankl: String?,
    val bankn: String?,
    val accountYear: String?,
    val surecpSlstmtNo: String?,
    val depositDt: String,
    val depositAmt: BigDecimal,
    val outamt: BigDecimal?,
    val crcyCd: String?,
    val remark: String?
)
