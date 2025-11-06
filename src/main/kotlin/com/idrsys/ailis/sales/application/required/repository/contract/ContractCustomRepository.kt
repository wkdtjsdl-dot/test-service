package com.idrsys.ailis.sales.application.required.repository.contract

import com.idrsys.ailis.sales.application.dto.query.ContractWithDetails
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.domain.model.Contract
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface ContractCustomRepository {
    fun findContracts(searchParam: ContractSearchParam, pageable: Pageable?): Flow<ContractWithDetails>
    suspend fun countContracts(searchParam: ContractSearchParam): Long
    suspend fun findContractById(custCntrId: Long): ContractWithDetails?
    suspend fun findDomainById(id: Long): Contract?
}
