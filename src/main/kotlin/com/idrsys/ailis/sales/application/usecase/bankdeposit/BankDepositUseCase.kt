package com.idrsys.ailis.sales.application.usecase.bankdeposit

import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankDepositBatchCommand
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult

interface BankDepositUseCase {

    /**
     * SAP RFC ZFI_IF_RE_020 호출하여 은행 입금 내역 조회
     *
     * IT_712에 bankAccounts 목록을 한 번에 전달하고 IT_713에서 전체 결과를 수신
     *
     * @param startDt 조회 시작일자 (yyyyMMdd)
     * @param endDt   조회 종료일자 (yyyyMMdd)
     * @param bankAccounts 조회할 은행 계좌 목록 (BANKL, BANKN)
     */
    suspend fun fetchFromSap(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult>

    /**
     * 은행 입금 내역 배치 저장
     *
     * 가수금전표번호(surecpSlstmtNo / BELNR) 기준 중복 체크 후 신규 건만 저장
     *
     * @return 실제 저장된 건수
     */
    suspend fun batchSave(commands: List<BankDepositBatchCommand>): Int
}
