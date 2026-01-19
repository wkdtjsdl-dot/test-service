package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.domain.model.BankDeposit
import com.idrsys.ailis.sales.generated.jooq.tables.SblBankDeposit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class BankDepositRepositoryImpl(
    private val bankDepositDataRepository: BankDepositDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : BankDepositRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(bankDeposit: BankDeposit): BankDeposit {
        return bankDepositDataRepository.save(bankDeposit)
    }

    override suspend fun findById(id: String): BankDeposit? {
        return bankDepositDataRepository.findById(id)
    }

    override suspend fun delete(bankDeposit: BankDeposit) {
        bankDepositDataRepository.delete(bankDeposit)
    }

    override fun findBankDeposits(searchParam: BankDepositSearchParam): Flow<BankDeposit> {
        val table = SblBankDeposit.SBL_BANK_DEPOSIT
        var condition = DSL.noCondition()

        condition = condition.and(table.DEPOSIT_DT.ge(searchParam.startDt))
        condition = condition.and(table.DEPOSIT_DT.le(searchParam.endDt))
        searchParam.regYn?.let {
            condition = condition.and(table.REG_YN.eq(it))
        }

        val query = dslContext.selectFrom(table).where(condition)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = executeSpec.bind(index, value)
        }

        return executeSpec
            .fetch()
            .all()
            .map { row ->
                BankDeposit(
                    bankDepositId = row[table.BANK_DEPOSIT_ID.name] as String,
                    accountDivCd = row[table.ACCOUNT_DIV_CD.name] as String?,
                    accountNo = row[table.ACCOUNT_NO.name] as String?,
                    accountYear = row[table.ACCOUNT_YEAR.name] as String?,
                    surecpSlstmtNo = row[table.SURECP_SLSTMT_NO.name] as String?,
                    depositDt = row[table.DEPOSIT_DT.name] as LocalDate,
                    depositAmt = row[table.DEPOSIT_AMT.name] as BigDecimal,
                    outamt = row[table.OUTAMT.name] as BigDecimal?,
                    crcyCd = row[table.CRCY_CD.name] as String?,
                    remark = row[table.REMARK.name] as String?,
                    regYn = row[table.REG_YN.name] as Boolean,
                    creator = row[table.CREATOR.name] as String,
                    createDtime = row[table.CREATE_DTIME.name] as LocalDateTime,
                    updater = row[table.UPDATER.name] as String,
                    updateDtime = row[table.UPDATE_DTIME.name] as LocalDateTime
                )
            }
            .asFlow()
    }

    override suspend fun findBankDepositById(bankDepositId: String): BankDeposit? {
        return bankDepositDataRepository.findById(bankDepositId)
    }

    override suspend fun updateRegYn(bankDepositId: String, regYn: Boolean, updater: String) {
        // TODO: Implement with jOOQ when needed
    }
}