package com.idrsys.ailis.sales.adapter.repository.contract

import com.idrsys.ailis.sales.application.required.repository.contract.ContractRepository
import com.idrsys.ailis.sales.domain.model.Contract
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ContractDataRepository : CoroutineCrudRepository<Contract, Long>

@Repository
class ContractRepositoryImpl(
    private val contractDataRepository: ContractDataRepository
) : ContractRepository {
    override suspend fun save(contract: Contract): Contract {
        return contractDataRepository.save(contract)
    }
}
