package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.repository.contract.ContractCustomRepository
import com.idrsys.ailis.sales.application.required.repository.contract.ContractRepository
import com.idrsys.ailis.sales.application.usecase.contract.ContractUseCase
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
    private val baseServicePort: BaseServicePort,
) : ContractUseCase {

    override suspend fun getContractPage(searchParam: ContractSearchParam, pageable: Pageable): Page<ContractListResponse> {
        val total = contractCustomRepository.countContracts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val contracts = contractCustomRepository.findContracts(searchParam, pageable).map({ dto ->
            val cntrPicNm = dto.cntrPicId?.let { baseServicePort.getUser(it)?.userNm }
            contractMapper.toListResponse(dto.copy(cntrPicNm = cntrPicNm))
        }).toList()

        return PageImpl(contracts, pageable, total)
    }

    override suspend fun getContractDetail(custCntrId: Long): ContractResponse {
        val dto = contractCustomRepository.findContractById(custCntrId)
            ?: throw NoSuchElementException("Contract not found with id: $custCntrId")

        val cntrPicNm = dto.cntrPicId?.let { baseServicePort.getUser(it)?.userNm }
        return contractMapper.toResponse(dto.copy(cntrPicNm = cntrPicNm))
    }

    override suspend fun createContract(custMstId: String, command: ContractCommand, adminId: String): ContractResponse {
        // 1. 첨부파일 처리: atchFiles가 있으면 base-service 호출
        val finalAtchGrupId = if (command.atchFiles != null) {
            val response = baseServicePort.saveAttachedFiles(command.atchFiles, adminId)
            response?.attachedFileGroupId ?: command.atchGrupId
        } else {
            command.atchGrupId
        }

        // 2. atchGrupId를 finalAtchGrupId로 교체한 command 생성
        val finalCommand = command.copy(atchGrupId = finalAtchGrupId)

        // 3. Contract 생성 및 저장
        val now = LocalDateTime.now()
        val contract = contractMapper.toDomain(finalCommand, custMstId, adminId, now).apply { setAsNew() }
        val savedContract = contractRepository.save(contract)
        return contractMapper.toResponse(savedContract)
    }

    override suspend fun updateContract(custMstId: String, custCntrId: Long, command: ContractCommand, adminId: String): ContractResponse {
        val contract = contractCustomRepository.findDomainById(custCntrId)
            ?: throw NoSuchElementException("Contract not found with id: $custCntrId")

        // 1. 첨부파일 처리: atchFiles가 있으면 base-service 호출
        val finalAtchGrupId = if (command.atchFiles != null) {
            val response = baseServicePort.saveAttachedFiles(command.atchFiles, adminId)
            response?.attachedFileGroupId ?: command.atchGrupId
        } else {
            command.atchGrupId
        }

        // 2. atchGrupId를 finalAtchGrupId로 교체한 command 생성
        val finalCommand = command.copy(atchGrupId = finalAtchGrupId)

        // 3. Contract 업데이트 및 저장
        contract.update(finalCommand, adminId)

        val updatedContract = contractRepository.save(contract)
        return contractMapper.toResponse(updatedContract)
    }

    // 첨부파일 삭제 생기면 추가적인 수정 예정
    override suspend fun deleteContract(custMstId: String, custCntrId: Long, adminId: String): Boolean {
        contractCustomRepository.findDomainById(custCntrId)
            ?: throw NoSuchElementException("Contract not found with id: $custCntrId")
        return contractRepository.delete(custCntrId)
    }
}
