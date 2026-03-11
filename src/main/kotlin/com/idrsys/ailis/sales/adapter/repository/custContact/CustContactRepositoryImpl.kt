package com.idrsys.ailis.sales.adapter.repository.custContact

import com.idrsys.ailis.sales.application.required.repository.custContact.CustContactRepository
import com.idrsys.ailis.sales.domain.model.CustContact
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustContactDataRepository : CoroutineCrudRepository<CustContact, Long>

@Repository
class CustContactRepositoryImpl(
    private val custContactDataRepository: CustContactDataRepository
) : CustContactRepository {
    override suspend fun save(custContact: CustContact): CustContact {
        return custContactDataRepository.save(custContact)
    }

    override suspend fun findById(id: Long): CustContact? {
        return custContactDataRepository.findById(id)
    }

    override suspend fun deleteById(id: Long) {
        custContactDataRepository.deleteById(id)
    }
}
