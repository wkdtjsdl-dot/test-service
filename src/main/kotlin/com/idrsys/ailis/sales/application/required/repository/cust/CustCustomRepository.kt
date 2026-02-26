package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.*
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import com.idrsys.ailis.sales.domain.model.Cust
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustCustomRepository {
    suspend fun findCustMstIdByCustCd(custCd: String): String?
    suspend fun findCustMstIdsByCustCds(custCds: List<String>): Map<String, String>
    fun findCustsWithSalsPicInfo(searchParam: CustSearchParam,pageable: Pageable): Flow<CustWithSalsPicInfo>
    fun findMyCustsWithSalsPicInfo(searchParam: CustSearchParam, pageable: Pageable, empUserId: String): Flow<CustWithSalsPicInfo>
    suspend fun countCusts(searchParam: CustSearchParam): Long
    suspend fun countMyCusts(searchParam: CustSearchParam, empUserId: String): Long
    suspend fun existByCustCd(custCd: String): Boolean
    fun findCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteInfo>
    fun findCustSimple(): Flow<CustCdNmAutoCompleteInfo>
    fun findRprsCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteInfo>
    suspend fun findCustDetailInfoByCustMstId(custMstId: String): CustDetailInfo?
    fun findDirectAcctCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteInfo>
    fun findAllWithCareInstId(): Flow<CustCareInstId>
    suspend fun findCustList(searchParam: CustSearchParam): Flow<CustBasicInfo>
    fun findCustTstMpgsByCustMstId(custMstId: String): Flow<TestCodeMappingQuery>

    fun findMyCustList(searchParam: CustSearchParam, empUserId: String): Flow<CustBasicInfo>
    fun findMyDirectAcctCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam, empUserId: String): Flow<DirectAcctCdNmAutoCompleteInfo>
    fun findMyCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam, empUserId: String): Flow<CustCdNmAutoCompleteInfo>
    suspend fun findInterfaceConfigByCustCd(custCd: String): ExcelConfigQuery?
    fun findExcelFieldsByCustCd(custCd: String): Flow<IfFieldInfoResponse>

    /**
     * Find customer billing info by customer codes (batch query)
     * @param custCds list of customer codes
     * @return map of custCd to CustBillingInfo
     */
    suspend fun findCustNmMapByCustCds(custCds: List<String>): Map<String, CustBillingInfo>

    /**
     * Find direct account codes by foreign account flag
     * @param frgnAcctYn true for foreign accounts, false for domestic accounts
     * @return list of direct account codes (cust_cd where cust_div_cd = 'CSDV_DA')
     */
    suspend fun findDirectAcctCdsByFrgnAcctYn(frgnAcctYn: Boolean): List<String>

    /**
     * Find customer by external authentication key
     * @param extnAuthKey external authentication key
     * @return Cust entity or null if not found
     */
    suspend fun findByExtnAuthKey(extnAuthKey: String): Cust?
}
