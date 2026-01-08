package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.BankDepositSearchParam
import com.idrsys.ailis.sales.application.required.repository.collection.BankDepositRepository
import com.idrsys.ailis.sales.domain.model.BankDeposit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jooq.DSLContext
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

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

    // Custom query operations (implemented with jOOQ)
    override fun findBankDeposits(searchParam: BankDepositSearchParam, pageable: Pageable): Flow<BankDeposit> {
        // TODO: Implement with jOOQ when needed
        return emptyFlow()
    }

    override suspend fun countBankDeposits(searchParam: BankDepositSearchParam): Long {
        // TODO: Implement with jOOQ when needed
        return 0L
    }

    override suspend fun findBankDepositById(bankDepositId: String): BankDeposit? {
        return bankDepositDataRepository.findById(bankDepositId)
    }

    override suspend fun updateRegYn(bankDepositId: String, regYn: Boolean, updater: String) {
        // TODO: Implement with jOOQ when needed
    }
}