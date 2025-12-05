package com.idrsys.ailis.sales.application.required.repository.contract

import com.idrsys.ailis.sales.domain.model.Contract

interface ContractRepository {
    suspend fun save(contract: Contract): Contract
    suspend fun findById(id: Long): Contract?
    suspend fun delete(id: Long): Boolean
}
