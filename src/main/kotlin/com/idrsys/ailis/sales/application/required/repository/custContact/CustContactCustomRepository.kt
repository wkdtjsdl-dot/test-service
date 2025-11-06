package com.idrsys.ailis.sales.application.required.repository.custContact

import com.idrsys.ailis.sales.application.dto.query.CustContactQuery
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.domain.model.CustContact
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustContactCustomRepository {
    fun findCustContacts(searchParam: CustContactSearchParam, pageable: Pageable?): Flow<CustContactQuery>
    suspend fun countCustContacts(searchParam: CustContactSearchParam): Long
    suspend fun findCustContactById(custContactId: Long): CustContactQuery?
    suspend fun findDomainById(id: Long): CustContact?
}
