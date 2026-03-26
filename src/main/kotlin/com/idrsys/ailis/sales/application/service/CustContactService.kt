package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustContactPhnoResponse
import com.idrsys.ailis.sales.application.dto.response.CustContactResponse
import com.idrsys.ailis.sales.application.required.repository.custContact.CustContactCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custContact.CustContactRepository
import com.idrsys.ailis.sales.application.usecase.custContact.CustContactUseCase
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.shared.mapper.CustContactMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustContactService(
    private val custContactRepository: CustContactRepository,
    private val custContactCustomRepository: CustContactCustomRepository,
    private val custContactMapper: CustContactMapper,
    private val baseServicePort: BaseServicePort,
) : CustContactUseCase {

    override suspend fun getCustContactPage(searchParam: CustContactSearchParam, pageable: Pageable): Page<CustContactResponse> {
        val total = custContactCustomRepository.countCustContacts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custContacts = custContactCustomRepository.findCustContacts(searchParam, pageable).map { dto ->
            val empNm = dto.creator.let { baseServicePort.getUser(it)?.userNm }
            custContactMapper.toResponseFromQuery(dto.copy(empNm = empNm))
        }.toList()

        return PageImpl(custContacts, pageable, total)
    }

    override suspend fun getCustContactDetail(custContactId: Long): CustContactResponse {
        val dto = custContactCustomRepository.findCustContactById(custContactId)
            ?: throw NoSuchElementException("CustContact not found with id: $custContactId")

        val empNm = dto.creator.let { baseServicePort.getUser(it)?.userNm }
        return custContactMapper.toResponseFromQuery(dto.copy(empNm = empNm))
    }

    override suspend fun createCustContact(command: CustContactCommand, adminId: String): CustContactResponse {
        val now = LocalDateTime.now()
        val custContact = custContactMapper.toDomain(command, adminId, now).apply { setAsNew() }
        val savedCustContact = custContactRepository.save(custContact)
        return custContactMapper.toResponse(savedCustContact)
    }

    override suspend fun updateCustContact(custContactId: Long, command: CustContactCommand, adminId: String): CustContactResponse {
        val custContact = custContactCustomRepository.findDomainById(custContactId)
            ?: throw NoSuchElementException("CustContact not found with id: $custContactId")

        custContact.update(command, adminId)

        val updatedCustContact = custContactRepository.save(custContact)
        return custContactMapper.toResponse(updatedCustContact)
    }

    override suspend fun deleteCustContact(custContactId: Long) {
        custContactRepository.deleteById(custContactId)
    }

    override suspend fun getPhnosByCustCds(custCdList: List<String>): List<CustContactPhnoResponse> {
        return custContactCustomRepository.findPhnosByCustCds(custCdList)
    }
}
