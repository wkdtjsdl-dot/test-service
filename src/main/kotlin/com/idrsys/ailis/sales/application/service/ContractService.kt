package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import com.idrsys.ailis.sales.application.required.repository.contract.ContractCustomRepository
import com.idrsys.ailis.sales.application.required.repository.contract.ContractRepository
import com.idrsys.ailis.sales.application.usecase.contract.ContractUseCase
import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.domain.model.Contract
import com.idrsys.ailis.sales.shared.mapper.ContractMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContractService(
    private val contractRepository: ContractRepository,
    private val contractCustomRepository: ContractCustomRepository,
    private val contractMapper: ContractMapper,
    private val baseServiceClient: BaseServiceClient,
) : ContractUseCase {

    override suspend fun getContractPage(searchParam: ContractSearchParam, pageable: Pageable): Page<ContractListResponse> {
        val total = contractCustomRepository.countContracts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val contracts = contractCustomRepository.findContracts(searchParam, pageable).map { dto ->
            val cntrPicNm = dto.cntrPicId?.let { baseServiceClient.getUser(it)?.userNm }
            contractMapper.toListResponse(dto.copy(cntrPicNm = cntrPicNm))
        }.toList()

        return PageImpl(contracts, pageable, total)
    }

    override suspend fun getContractDetail(custCntrId: Long): ContractResponse {
        val dto = contractCustomRepository.findContractById(custCntrId)
            ?: throw NoSuchElementException("Contract not found with id: $custCntrId")

        val cntrPicNm = dto.cntrPicId?.let { baseServiceClient.getUser(it)?.userNm }
        return contractMapper.toResponse(dto.copy(cntrPicNm = cntrPicNm))
    }

    override suspend fun createContract(custMstId: String, command: ContractCommand, adminId: String): ContractResponse {
        val now = LocalDateTime.now()
        val contract = Contract(
            custMstId = custMstId,
            custCd = command.custCd,
            cntrNo = command.cntrNo,
            cntrDt = command.cntrDt,
            cntrStartDt = command.cntrStartDt,
            cntrEndDt = command.cntrEndDt,
            cntrType = command.cntrType,
            recntrMonth = command.recntrMonth,
            cntrNm = command.cntrNm,
            cntrCont = command.cntrCont,
            cntrPicId = command.cntrPicId,
            atchGrupId = command.atchGrupId,
            useYn = command.useYn,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        ).apply { setAsNew() }

        val savedContract = contractRepository.save(contract)
        return contractMapper.toResponse(savedContract)
    }

    override suspend fun updateContract(custMstId: String, custCntrId: Long, command: ContractCommand, adminId: String): ContractResponse {
        val contract = contractCustomRepository.findDomainById(custCntrId)
            ?: throw NoSuchElementException("Contract not found with id: $custCntrId")

        contract.update(command, adminId)

        val updatedContract = contractRepository.save(contract)
        return contractMapper.toResponse(updatedContract)
    }
}
