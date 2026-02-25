package com.idrsys.ailis.sales.application.usecase.cust

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustAtchFileUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustReqIfMethodUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse
import com.idrsys.ailis.sales.domain.model.Cust
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustUseCase {
    suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> // list
    suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable, empUserId: String, roleCodes: List<String>): Page<CustListResponse> // list with auth
    suspend fun getCusts(searchParam: CustSearchParam): Flow<CustListResponse> // excel
    suspend fun findCustByCustMstId(custMstId: String): CustResponse
    suspend fun findTstItemsByCustMstId(custMstId: String): Flow<TstServiceTstItemsResponse>
    suspend fun registerCust(command: CustRegisterCommand, creator: String): Cust
    suspend fun updateCust(custMstId: String, command: CustUpdateCommand, updater: String): Cust
    suspend fun updateCustAtchFile(custMstId: String, command: CustAtchFileUpdateCommand, updater: String)
    suspend fun updateCustReqIfMethod(custMstId: String, command: CustReqIfMethodUpdateCommand, updater: String)
    suspend fun isCustCdExists(custCd: String): Boolean
    fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteResponse>
    fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam, empUserId: String, roles: List<String>): Flow<CustCdNmAutoCompleteResponse>
    fun getRprsCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteResponse>
    fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteResponse>
    suspend fun getCustList(searchParam: CustSearchParam): List<CustBasicResponse>
    fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam, empUserId: String, roles: List<String>): Flow<DirectAcctCdNmAutoCompleteResponse>
    suspend fun getCustList(searchParam: CustSearchParam, empUserId: String, roles: List<String>): List<CustBasicResponse>
    suspend fun findCustTstMpgsByCustMstId(custMstId: String): Flow<TestCodeMappingResponse>
    suspend fun getInterfaceConfigByCustCd(custCd: String): ExcelConfigResponse
    suspend fun getExcelFieldsByCustCd(custCd: String): Flow<IfFieldInfoResponse>

    /**
     * Validate external authentication key
     * @param extnAuthKey external authentication key
     * @return AuthKeyValidateResponse or null if invalid
     */
    suspend fun validateAuthKey(extnAuthKey: String): com.idrsys.ailis.sales.application.dto.response.AuthKeyValidateResponse?
}
