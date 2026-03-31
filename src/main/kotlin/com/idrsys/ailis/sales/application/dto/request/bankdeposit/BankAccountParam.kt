package com.idrsys.ailis.sales.application.dto.request.bankdeposit

/**
 * SAP IT_712 테이블 Row — 조회할 은행 계좌 정보
 *
 * @param bankl 은행번호 (SAP BANKL)
 * @param bankn 계좌번호 (SAP BANKN)
 */
data class BankAccountParam(
    val bankl: String,
    val bankn: String
)
