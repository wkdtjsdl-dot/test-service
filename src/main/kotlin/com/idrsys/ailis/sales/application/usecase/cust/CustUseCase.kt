package com.idrsys.ailis.sales.application.usecase.cust

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustBasicResponse
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.RprsCustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.dto.response.DirectAcctCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.domain.model.Cust
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustUseCase {
    suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> // list
    suspend fun getCusts(searchParam: CustSearchParam): Flow<CustListResponse> // excel
    suspend fun findCustByCustMstId(custMstId: String): CustResponse
    suspend fun registerCust(command: CustRegisterCommand, creator: String): Cust
    suspend fun updateCust(custMstId: String, command: CustUpdateCommand, updater: String): Cust
    suspend fun isCustCdExists(custCd: String): Boolean
    fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteResponse>
    fun getRprsCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteResponse>
    fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteResponse>
    suspend fun getCustList(searchParam: CustSearchParam): List<CustBasicResponse>
}
