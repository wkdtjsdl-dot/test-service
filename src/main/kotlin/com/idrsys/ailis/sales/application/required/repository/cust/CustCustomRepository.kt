package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustSearchCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.query.*
import com.idrsys.ailis.sales.application.dto.response.CustCdNmResponse
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import com.idrsys.ailis.sales.domain.model.Cust
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustCustomRepository {
    suspend fun findByCustCd(custCd: String): Cust?
    suspend fun findCustMstIdByCustCd(custCd: String): String?
    suspend fun findCustMstIdsByCustCds(custCds: List<String>): Map<String, String>
    fun findCustsWithSalsPicInfo(searchParam: CustSearchParam,pageable: Pageable): Flow<CustWithSalsPicInfo>
    fun findMyCustsWithSalsPicInfo(searchParam: CustSearchParam, pageable: Pageable, empUserId: String): Flow<CustWithSalsPicInfo>
    suspend fun searchInnerCusts(searchParam: CustSearchCommand): List<Cust>
    suspend fun countCusts(searchParam: CustSearchParam): Long
    suspend fun countMyCusts(searchParam: CustSearchParam, empUserId: String): Long
    suspend fun existByCustCd(custCd: String): Boolean
    suspend fun deleteByCustCd(custCd: String)
    fun findCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteInfo>
    fun findCustSimple(): Flow<CustCdNmAutoCompleteInfo>
    fun findCustSimple(bzoffiCd: String?): Flow<CustCdNmAutoCompleteInfo>
    fun findCustSimple(bzoffiCd: String?, empUserId: String?): Flow<CustCdNmAutoCompleteInfo>
    fun findRprsCustCdNmAutoComplete(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteInfo>
    suspend fun findCustDetailInfoByCustMstId(custMstId: String): CustDetailInfo?
    suspend fun findCustDetailInfoByCustCd(custCd: String): CustDetailInfo?
    suspend fun findCustNmByCustCd(custCds: List<String>): Map<String, CustCdNmResponse>
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
     * Find customer codes by foreign account flag
     * @param frgnAcctYn true for foreign accounts, false for domestic accounts
     * @param bzoffiCd optional business office code filter
     * @return list of cust_cds
     */
    suspend fun findCustCdsByFrgnAcctYn(frgnAcctYn: Boolean, bzoffiCd: String? = null): List<String>

    /**
     * Find customer by external authentication key
     * @param extnAuthKey external authentication key
     * @return Cust entity or null if not found
     */
    suspend fun findByExtnAuthKey(extnAuthKey: String): Cust?

    /**
     * Find rprs billing info (rprsCustCd + rprsAcctBillCombPublYn) by customer codes
     * @param custCds list of customer codes
     * @return map of custCd to RprsBillingInfo
     */
    suspend fun findRprsBillingInfoByCustCds(custCds: List<String>): Map<String, RprsBillingInfo>

    /**
     * Find cust_cds whose bzoffi_cd is in the given list (excluded from billing closing)
     * @param bzoffiCds list of dept_cds to match against bzoffi_cd
     * @return set of cust_cds belonging to those departments
     */
    suspend fun findCustCdsByBzoffiCds(bzoffiCds: List<String>): Set<String>

    suspend fun findNoBillPublCustCds(): Set<String>

    /**
     * Find all custCds belonging to the given billing key.
     * Returns the billingKey itself plus all custCds where
     * rprs_cust_cd = billingKey AND rprs_acct_bill_comb_publ_yn = true.
     * Always returns at least [billingKey].
     */
    suspend fun findConstituentCustCds(billingKey: String): List<String>
}
