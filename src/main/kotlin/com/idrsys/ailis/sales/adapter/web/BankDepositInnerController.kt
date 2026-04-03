package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankDepositBatchCommand
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult
import com.idrsys.ailis.sales.application.usecase.bankdeposit.BankDepositUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inner/bank-deposits")
@Tag(name = "BankDepositInnerController", description = "은행 입금정보 내부 Controller")
class BankDepositInnerController(
    private val bankDepositUseCase: BankDepositUseCase
) {

    /**
     * SAP RFC ZFI_IF_RE_020 호출하여 은행 입금 내역 조회
     *
     * IT_712에 은행 계좌 목록을 한 번에 전달하고 IT_713에서 전체 결과 수신
     * data-coll-service 배치의 Reader 단계에서 호출
     */
    @PostMapping("/sap")
    @Operation(summary = "fetchBankDepositsFromSap", description = "SAP 은행 입금 내역 조회 (내부 API)")
    suspend fun fetchBankDepositsFromSap(
        @RequestParam startDt: String,
        @RequestParam endDt: String,
        @RequestBody bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult> {
        return bankDepositUseCase.fetchFromSap(startDt, endDt, bankAccounts)
    }

    /**
     * 은행 입금 내역 배치 저장
     *
     * 가수금전표번호(BELNR) 기준 중복 체크 후 신규 건만 저장
     * data-coll-service 배치의 Writer 단계에서 호출
     */
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "batchSaveBankDeposits", description = "은행 입금 내역 배치 저장 (내부 API)")
    suspend fun batchSaveBankDeposits(
        @RequestBody commands: List<BankDepositBatchCommand>
    ): Int {
        return bankDepositUseCase.batchSave(commands)
    }
}
