package com.idrsys.ailis.sales.application.usecase.custContact

import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustContactResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustContactUseCase {
    suspend fun getCustContactPage(searchParam: CustContactSearchParam, pageable: Pageable): Page<CustContactResponse>
    suspend fun getCustContactDetail(custMstId: String, custContactId: Long): CustContactResponse
    suspend fun createCustContact(command: CustContactCommand, adminId: String): CustContactResponse
    suspend fun updateCustContact(custContactId: Long, command: CustContactCommand, adminId: String): CustContactResponse
}
