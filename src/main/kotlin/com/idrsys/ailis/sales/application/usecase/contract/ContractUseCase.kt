package com.idrsys.ailis.sales.application.usecase.contract

import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContractUseCase {
    suspend fun getContractPage(searchParam: ContractSearchParam, pageable: Pageable): Page<ContractListResponse>
    suspend fun getContractDetail(custCntrId: Long): ContractResponse
    suspend fun createContract(custMstId: String, command: ContractCommand, adminId: String): ContractResponse
    suspend fun updateContract(custMstId: String, custCntrId: Long, command: ContractCommand, adminId: String): ContractResponse
    suspend fun deleteContract(custMstId: String, custCntrId: Long, adminId: String): Boolean
}
