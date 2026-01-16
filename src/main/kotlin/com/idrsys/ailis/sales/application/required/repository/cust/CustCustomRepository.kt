package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.*
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustCustomRepository {
    suspend fun findCustMstIdByCustCd(custCd: String): String?
    fun findCustsWithSalsPicInfo(searchParam: CustSearchParam,pageable: Pageable): Flow<CustWithSalsPicInfo>
    suspend fun countCusts(searchParam: CustSearchParam): Long
    suspend fun existByCustCd(custCd: String): Boolean
    fun findCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteInfo>
    fun findRprsCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteInfo>
    suspend fun findCustDetailInfoByCustMstId(custMstId: String): CustDetailInfo?
    fun findDirectAcctCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteInfo>
    fun findAllWithCareInstId(): Flow<CustCareInstId>
    suspend fun findCustList(searchParam: CustSearchParam): Flow<CustBasicInfo>
    fun findCustTstMpgsByCustMstId(custMstId: String): Flow<TestCodeMappingQuery>

    /**
     * Find customer names by customer codes (batch query)
     * @param custCds list of customer codes
     * @return map of custCd to custNm
     */
    suspend fun findCustNmMapByCustCds(custCds: List<String>): Map<String, String>
}