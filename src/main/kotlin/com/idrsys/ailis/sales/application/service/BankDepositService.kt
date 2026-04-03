package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.sap.SapRfcClient
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankAccountParam
import com.idrsys.ailis.sales.application.dto.request.bankdeposit.BankDepositBatchCommand
import com.idrsys.ailis.sales.application.dto.response.sap.SapBankDepositResult
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.application.usecase.bankdeposit.BankDepositUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class BankDepositService(
    private val sapRfcClient: SapRfcClient,
    private val bankDepositRepository: BankDepositRepository
) : BankDepositUseCase {

    override suspend fun fetchFromSap(
        startDt: String,
        endDt: String,
        bankAccounts: List<BankAccountParam>
    ): List<SapBankDepositResult> = withContext(Dispatchers.IO) {
        sapRfcClient.executeIfRe020(startDt, endDt, bankAccounts)
    }

    override suspend fun batchSave(commands: List<BankDepositBatchCommand>): Int {
        return bankDepositRepository.batchInsert(commands, BATCH_SYSTEM_USER)
    }

    companion object {
        private const val BATCH_SYSTEM_USER = "BATCH_SYSTEM"
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")

        fun SapBankDepositResult.toBatchCommand(): BankDepositBatchCommand? {
            val depositDate = runCatching { LocalDate.parse(depositDt, DATE_FORMATTER) }.getOrNull()
                ?: return null
            return BankDepositBatchCommand(
                accountDivCd = bankl,
                accountNo = bankn,
                accountYear = accountYear,
                surecpSlstmtNo = surecpSlstmtNo,
                depositDt = depositDate,
                depositAmt = depositAmt,
                outamt = outamt,
                crcyCd = crcyCd,
                remark = remark
            )
        }
    }
}
