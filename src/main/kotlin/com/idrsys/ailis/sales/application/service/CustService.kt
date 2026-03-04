package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustAtchFileUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.cust.CustReqIfMethodUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.application.required.external.TstServicePort
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustMstHstRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemCustomRepository
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.shared.mapper.CustMapper
import com.idrsys.ailis.sales.shared.mapper.TestCodeMappingMapper
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = false)
class CustService(
    private val custCustomRepository: CustCustomRepository,
    private val custRepository: CustRepository,
    private val custMstHistRepository: CustMstHstRepository,
    private val custReqPossTstItemCustomRepository: CustReqPossTstItemCustomRepository,
    private val custMapper: CustMapper,
    private val baseServicePort: BaseServicePort,
    private val tstServicePort: TstServicePort,
    private val hospitalDataService: HospitalDataService,
    private val testCodeMappingMapper: TestCodeMappingMapper,
) : CustUseCase {
    @Transactional(readOnly = true)
    override suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> {
        val systemCodeMaps = fetchSystemCodeMaps()

        // 검색어에서 받아온 값(name or id)을 id로 정리해서 쿼리에 던짐
        var finalSearchParam = searchParam
        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val users = baseServicePort.getUsers() ?: emptyList()
            val matchedUserIds = users
                .filter { it.userNm.contains(searchParam.empUserIdNm, ignoreCase = true) ||
                          it.userId.contains(searchParam.empUserIdNm, ignoreCase = true) }
                .map { it.userId }
            finalSearchParam = searchParam.copy(empUserIds = matchedUserIds)
        }
        val total = custCustomRepository.countCusts(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custsFromRepo = custCustomRepository.findCustsWithSalsPicInfo(finalSearchParam, pageable).toList()

        val userIds = custsFromRepo.flatMap { cust ->
            cust.salsPicInfo?.split(",")
                ?.mapNotNull { pair ->
                    val parts = pair.split("=")
                    if (parts.size == 2) parts[1] else null
                } ?: emptyList()
        }.distinct()

        val (deptNameById, userNameById) = fetchLookupMaps(userIds)

        val responses = custsFromRepo.map { custWithSalsPicInfo ->
            val initialResponse = custMapper.toListResponse(custWithSalsPicInfo)
            transformCustListResponse(initialResponse, deptNameById, userNameById, systemCodeMaps)
        }

        return PageImpl(responses, pageable, total)
    }

    @Transactional(readOnly = true)
    override suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable, empUserId: String, roleCodes: List<String>): Page<CustListResponse> {
        val systemCodeMaps = fetchSystemCodeMaps()

        // 검색어에서 받아온 값(name or id)을 id로 정리해서 쿼리에 던짐
        var finalSearchParam = searchParam
        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val users = baseServicePort.getUsers() ?: emptyList()
            val matchedUserIds = users
                .filter { it.userNm.contains(searchParam.empUserIdNm, ignoreCase = true) ||
                        it.userId.contains(searchParam.empUserIdNm, ignoreCase = true) }
                .map { it.userId }
            finalSearchParam = searchParam.copy(empUserIds = matchedUserIds)
        }
        if (isUserRoleSlcp(roleCodes)) {
            val user = baseServicePort.getUser(empUserId)
            finalSearchParam = finalSearchParam.copy(bzoffiCd = user?.deptCd)
        }

        val total = custCustomRepository.countCusts(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)
        val custsFromRepo = custCustomRepository.findCustsWithSalsPicInfo(finalSearchParam, pageable).toList()

        val userIds = custsFromRepo.flatMap { cust ->
            cust.salsPicInfo?.split(",")
                ?.mapNotNull { pair ->
                    val parts = pair.split("=")
                    if (parts.size == 2) parts[1] else null
                } ?: emptyList()
        }.distinct()

        val (deptNameById, userNameById) = fetchLookupMaps(userIds)

        val responses = custsFromRepo.map { custWithSalsPicInfo ->
            val initialResponse = custMapper.toListResponse(custWithSalsPicInfo)
            transformCustListResponse(initialResponse, deptNameById, userNameById, systemCodeMaps)
        }

        return PageImpl(responses, pageable, total)
    }

    @Transactional(readOnly = true)
    override suspend fun getCusts(searchParam: CustSearchParam): Flow<CustListResponse> {
        val systemCodeMaps = fetchSystemCodeMaps()

        // 검색어에서 받아온 값(name or id)을 id로 정리해서 쿼리에 던짐
        var finalSearchParam = searchParam
        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val users = baseServicePort.getUsers() ?: emptyList()
            val matchedUserIds = users
                .filter { it.userNm.contains(searchParam.empUserIdNm, ignoreCase = true) ||
                          it.userId.contains(searchParam.empUserIdNm, ignoreCase = true) }
                .map { it.userId }
            finalSearchParam = searchParam.copy(empUserIds = matchedUserIds)
        }

        val custsFromRepo = custCustomRepository.findCustsWithSalsPicInfo(finalSearchParam, Pageable.unpaged()).toList()

        val userIds = custsFromRepo.flatMap { cust ->
            cust.salsPicInfo?.split(",")
                ?.mapNotNull { pair ->
                    val parts = pair.split("=")
                    if (parts.size == 2) parts[1] else null
                } ?: emptyList()
        }.distinct()

        val (deptNameById, userNameById) = fetchLookupMaps(userIds)

        return flow {
            custsFromRepo.forEach { custWithSalsPicInfo ->
                val initialResponse = custMapper.toListResponse(custWithSalsPicInfo)
                val transformed = transformCustListResponse(initialResponse, deptNameById, userNameById, systemCodeMaps)
                emit(transformed)
            }
        }

    }

    @Transactional(readOnly = true)
    override suspend fun findCustByCustMstId(custMstId: String): CustResponse {
        val cust = custCustomRepository.findCustDetailInfoByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")

        val response = custMapper.toDetailResponse(cust)

        if (!response.careInstId.isNullOrBlank()) {
            try {
                val hospitalMst = hospitalDataService.getHospitalMstDetail(response.careInstId)
                return response.copy(careInstNm = hospitalMst.careInstNm)
            } catch (e: Exception) {
                e.stackTraceToString()
            }
        }

        return response
    }

    @Transactional(readOnly = true)
    override suspend fun findCustByCustCd(custCd: String): CustResponse {
        val cust = custCustomRepository.findCustDetailInfoByCustCd(custCd)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custCd")

        val response = custMapper.toDetailResponse(cust)

        if (!response.careInstId.isNullOrBlank()) {
            try {
                val hospitalMst = hospitalDataService.getHospitalMstDetail(response.careInstId)
                return response.copy(careInstNm = hospitalMst.careInstNm)
            } catch (e: Exception) {
                e.stackTraceToString()
            }
        }

        return response
    }

    override suspend fun findTstItemsByCustMstId(custMstId: String): Flow<TstServiceTstItemsResponse> {
        val custMst = custRepository.findByCustMstId(custMstId)

        return if (custMst?.reqPossTstLimitYn == true) {
            val allTestItemsMap = (tstServicePort.findAllTstItems() ?: emptyList())
                .associateBy { it.tstCd }

            val searchParam = CustReqPossTstItemSearchParam(custMstId = custMstId)
            custReqPossTstItemCustomRepository.findAllByCustMstId(searchParam)
                .map { allowedItem ->
                    TstServiceTstItemsResponse(
                        tstCd = allowedItem.tstCd,
                        tstNm = allTestItemsMap[allowedItem.tstCd]?.tstNm
                    )
                }
        } else {
            (tstServicePort.findAllTstItems() ?: emptyList()).asFlow()
        }
    }

    override suspend fun registerCust(command: CustRegisterCommand, creator: String): Cust {
        val custCd = command.custCd

        if (custCustomRepository.existByCustCd(custCd)) {
            throw UserDefinedException(
                "USER_ALREADY_EXIST", "이미 존재하는 고객코드입니다 : $custCd")
        }

        val now = LocalDateTime.now()
        val newCust = custMapper.toDomain(command, creator, now)
        val savedCust = custRepository.save(newCust)

        // Save history
        val hist = custMapper.toHistDomain(savedCust, command.updateReason)
        custMstHistRepository.save(hist)

        return savedCust
    }

    override suspend fun updateCust(custMstId: String, command: CustUpdateCommand, updater: String): Cust {
        val custCd = command.custCd

        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw UserDefinedException("USER_NOT_FOUND","고객을 찾을 수 없습니다: $custCd")

        cust.update(command, updater)
        val updatedCust = custRepository.save(cust)

        // Save history
        val hist = custMapper.toHistDomain(updatedCust, command.updateReason)
        custMstHistRepository.save(hist)

        return updatedCust
    }

    override suspend fun updateCustAtchFile(
        custMstId: String,
        command: CustAtchFileUpdateCommand,
        updater: String
    ) {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw UserDefinedException("CUST_NOT_FOUND", "고객 정보를 찾을 수 없습니다")

        // atchFileGrupId만 업데이트
        cust.updateAtchFileGrupId(command.atchFileGrupId, updater)

        custRepository.save(cust)
    }

    override suspend fun updateCustReqIfMethod(
        custMstId: String,
        command: CustReqIfMethodUpdateCommand,
        updater: String
    ) {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw UserDefinedException("CUST_NOT_FOUND", "고객 정보를 찾을 수 없습니다")

        cust.updateReqIfMethod(command.reqMethodCd, command.reqIfTypeCd, updater)

        custRepository.save(cust)
    }

    @Transactional(readOnly = true)
    override suspend fun isCustCdExists(custCd: String): Boolean {
        return custCustomRepository.existByCustCd(custCd)
    }

    @Transactional(readOnly = true)
    override fun getCustSimpleList(): Flow<CustCdNmAutoCompleteResponse> {
        return custCustomRepository.findCustSimple().map(custMapper::toCustCdNmAutoCompleteResponse)
    }

    @Transactional(readOnly = true)
    override fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findCustCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toCustCdNmAutoCompleteResponse)
    }

    @Transactional(readOnly = true)
    override fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam, empUserId: String, roles: List<String>): Flow<CustCdNmAutoCompleteResponse> {
        return if (isUserAdmin(roles)) {
            custCustomRepository.findCustCdNmAutoComplete(searchParam)
                .map(custMapper::toCustCdNmAutoCompleteResponse)
        } else {
            flow {
                val user = baseServicePort.getUser(empUserId)
                val modifiedParam = searchParam.copy(bzoffiCd = user?.deptCd)
                emitAll(
                    custCustomRepository.findCustCdNmAutoComplete(modifiedParam)
                        .map(custMapper::toCustCdNmAutoCompleteResponse)
                )
            }
        }
    }

    @Transactional(readOnly = true)
    override fun getRprsCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findRprsCustCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toRprsCustCdNmAutoCompleteResponse)
    }

    @Transactional(readOnly = true)
    override fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam, empUserId: String, roles: List<String>): Flow<DirectAcctCdNmAutoCompleteResponse> {
        return if (isUserAdmin(roles)) {
            val autoCompleteList = custCustomRepository.findDirectAcctCdNmAutoComplete(searchParam)
            autoCompleteList.map(custMapper::toDirectAcctCdNmAutoCompleteResponse)
        } else {
            val autoCompleteList = custCustomRepository.findMyDirectAcctCdNmAutoComplete(searchParam, empUserId)
            autoCompleteList.map(custMapper::toDirectAcctCdNmAutoCompleteResponse)
        }
    }

    @Transactional(readOnly = true)
    override suspend fun getCustList(searchParam: CustSearchParam, empUserId: String, roles: List<String>): List<CustBasicResponse> {
        // join 없는 cust_mst 최소한의 데이터
        return if (isUserAdmin(roles)) {
            custCustomRepository.findCustList(searchParam)
                .map(custMapper::toBasicResponse)
                .toList()
        } else {
            custCustomRepository.findMyCustList(searchParam, empUserId)
                .map(custMapper::toBasicResponse)
                .toList()
        }
    }

    @Transactional(readOnly = true)
    override suspend fun findCustTstMpgsByCustMstId(custMstId: String): Flow<TestCodeMappingResponse> {
        return custCustomRepository.findCustTstMpgsByCustMstId(custMstId)
            .map(testCodeMappingMapper::toResponse)
    }

    companion object {
        private val ADMIN_ROLE_CODES = setOf("RO_DPP")
    }

    private fun isUserAdmin(roles: List<String>): Boolean {
        return ADMIN_ROLE_CODES.any { roles.contains(it) }
    }

    private fun isUserRoleSlcp(roles: List<String>): Boolean {
        return roles.contains("RO_SLCP")
    }

    @Transactional(readOnly = true)
    override fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findDirectAcctCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toDirectAcctCdNmAutoCompleteResponse)
    }

    @Transactional(readOnly = true)
    override suspend fun getCustList(searchParam: CustSearchParam): List<CustBasicResponse> {
        return custCustomRepository.findCustList(searchParam)
            .map(custMapper::toBasicResponse)
            .toList()
    }

    @Transactional(readOnly = true)
    override suspend fun getInterfaceConfigByCustCd(custCd: String): ExcelConfigResponse {
        val configQuery = custCustomRepository.findInterfaceConfigByCustCd(custCd)
        return configQuery?.let {
            ExcelConfigResponse(
                headerInclYn = it.headerInclYn,
                skipRowCnt = it.skipRowCnt
            )
        } ?: ExcelConfigResponse(headerInclYn = false, skipRowCnt = 0)
    }

    @Transactional(readOnly = true)
    override suspend fun getExcelFieldsByCustCd(custCd: String): Flow<IfFieldInfoResponse> {
        return custCustomRepository.findExcelFieldsByCustCd(custCd)
    }

    private suspend fun fetchLookupMaps(userIds: List<String>): LookupMaps {
        val depts = baseServicePort.getDepartments() ?: emptyList()
        val deptNameById = depts.associate { it.deptCd to it.deptNm }

        val users = if (userIds.isNotEmpty()) {
            baseServicePort.getUsersByIds(userIds) ?: emptyList()
        } else {
            emptyList()
        }
        val userNameById = users.associate { it.userId to it.userNm }
        return LookupMaps(deptNameById, userNameById)
    }

    private suspend fun fetchSystemCodeMaps(): SystemCodeMaps {
        val systemCodes = baseServicePort.getChildrenSystemCodes(listOf("CSDV", "CSTP", "CSST")) ?: emptyMap()

        val custDivNameByCd = systemCodes["CSDV"]?.associate { it.cd to it.cdNm } ?: emptyMap()
        val custTypeNameByCd = systemCodes["CSTP"]?.associate { it.cd to it.cdNm } ?: emptyMap()
        val custStatNameByCd = systemCodes["CSST"]?.associate { it.cd to it.cdNm } ?: emptyMap()

        return SystemCodeMaps(custDivNameByCd, custTypeNameByCd, custStatNameByCd)
    }

    // 부서명, 사원명, 영업팀코드, 고객 코드 치환
    private suspend fun transformCustListResponse(
        custListResponse: CustListResponse,
        deptNameById: Map<String, String>,
        userNameById: Map<String, String>,
        systemCodeMaps: SystemCodeMaps
    ): CustListResponse {
        val deptNm = custListResponse.bzoffiCd?.let { deptNameById[it] }

        val transformedSalsPicInfo = custListResponse.salsPicInfo?.let { infoString ->
            val salesPicEntries = infoString.split(",").mapNotNull { pair ->
                val parts = pair.split("=")
                if (parts.size == 2) parts[0] to parts[1] else null
            }

            val filteredSalesPicEntries = if (custListResponse.frgnAcctYn == true) {
                salesPicEntries.filter { (rawTeamCode, _) -> rawTeamCode == "OBT" }
            } else {
                salesPicEntries.filter { (rawTeamCode, _) -> rawTeamCode in listOf("SLTM_TS-G", "SLTM_TS-C", "SLTM_TS-H") }
            }

            val substitutedAndNamedEntries = filteredSalesPicEntries.map { (rawTeamCode, userId) ->
                val substitutedTeamCode = when (rawTeamCode) {
                    "SLTM_TS-G" -> "G"
                    "SLTM_TS-C" -> "C"
                    "SLTM_TS-H" -> "H"
                    "OBT" -> "O"
                    else -> rawTeamCode
                }
                val userName = userNameById[userId] ?: userId
                substitutedTeamCode to userName
            }

            val sortedEntries = if (custListResponse.frgnAcctYn == true) {
                substitutedAndNamedEntries.sortedBy { (_, userName) -> userName }
            } else {
                substitutedAndNamedEntries.sortedWith(compareBy { (teamCode, _) ->
                    when (teamCode) {
                        "G" -> 1
                        "C" -> 2
                        "H" -> 3
                        else -> 4
                    }
                })
            }
            sortedEntries.joinToString(",") { (teamCode, userName) -> "$teamCode=$userName" }
        } ?: custListResponse.salsPicInfo

        val custDivNm = systemCodeMaps.custDivNameByCd[custListResponse.custDivCd] ?: custListResponse.custDivCd
        val custTypeNm = systemCodeMaps.custTypeNameByCd[custListResponse.custTypeCd] ?: custListResponse.custTypeCd
        val custStatNm = systemCodeMaps.custStatNameByCd[custListResponse.custStatCd] ?: custListResponse.custStatCd

        return custListResponse.copy(
            deptNm = deptNm,
            salsPicInfo = transformedSalsPicInfo,
            custDivCd = custDivNm,
            custTypeCd = custTypeNm,
            custStatCd = custStatNm
        )
    }

    data class LookupMaps(
        val deptNameById: Map<String, String>,
        val userNameById: Map<String, String>
    )

    data class SystemCodeMaps(
        val custDivNameByCd: Map<String, String>,
        val custTypeNameByCd: Map<String, String>,
        val custStatNameByCd: Map<String, String>
    )

    @Transactional(readOnly = true)
    override suspend fun validateAuthKey(extnAuthKey: String): AuthKeyValidateResponse? {
        val cust = custCustomRepository.findByExtnAuthKey(extnAuthKey)
            ?: return null

        // 거래처 상태가 비활성이거나 의뢰불가이면 null 반환
        if (cust.custStatCd != "CSST_N" || !cust.reqPossYn) {
            return null
        }

        return AuthKeyValidateResponse(
            custMstId = cust.custMstId!!,
            custCd = cust.custCd,
            custNm = cust.custNm,
            custStatCd = cust.custStatCd,
            reqPossYn = cust.reqPossYn
        )
    }
}
