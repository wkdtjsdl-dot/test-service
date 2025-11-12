package com.idrsys.ailis.sales.application.required.repository.custContact

import com.idrsys.ailis.sales.domain.model.CustContact

interface CustContactRepository {
    suspend fun save(custContact: CustContact): CustContact
    suspend fun findById(id: Long): CustContact?
    suspend fun deleteById(id: Long)
}
