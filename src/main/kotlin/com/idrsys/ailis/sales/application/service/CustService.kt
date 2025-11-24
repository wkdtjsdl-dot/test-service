package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.cust.CustAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.RprsCustCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.dto.response.DirectAcctCdNmAutoCompleteResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustMstHstRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.shared.mapper.CustMapper
import com.idrsys.ailis.sales.application.service.HospitalDataService
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustService(
    private val custCustomRepository: CustCustomRepository,
    private val custRepository: CustRepository,
    private val custMstHistRepository: CustMstHstRepository,
    private val custMapper: CustMapper,
    private val baseServiceClient: BaseServiceClient,
    private val hospitalDataService: HospitalDataService
) : CustUseCase {
    override suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> {
        val total = custCustomRepository.countCusts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custsFromRepo = custCustomRepository.findCustsWithSalsPicInfo(searchParam, pageable).toList()

        val (deptNameById, userNameById) = fetchLookupMaps()

        val responses = custsFromRepo.map { custWithSalsPicInfo ->
            val initialResponse = custMapper.toListResponse(custWithSalsPicInfo)
            transformCustListResponse(initialResponse, deptNameById, userNameById)
        }

        return PageImpl(responses, pageable, total)
    }

    override suspend fun getCusts(searchParam: CustSearchParam): Flow<CustListResponse> { // Make it suspend
        val custsFromRepo = custCustomRepository.findCustsWithSalsPicInfo(searchParam,Pageable.unpaged())

        val (deptNameById, userNameById) = fetchLookupMaps()

        return custsFromRepo
            .map(custMapper::toListResponse) // Map to CustListResponse first
            .map { initialResponse ->
                transformCustListResponse(initialResponse, deptNameById, userNameById)
            }
    }

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

    @Transactional(readOnly = false)
    override suspend fun registerCust(command: CustRegisterCommand, creator: String): Cust {
        val custCd = command.custCd

        if (custCustomRepository.existByCustCd(custCd)) {
            throw UserDefinedException(
                "USER_ALREADY_EXIST", "이미 존재하는 고객코드입니다 : $custCd")
        }

        val newCust = custMapper.toDomain(command, creator, LocalDateTime.now())
        val savedCust = custRepository.save(newCust)

        // Save history
        val hist = custMapper.toHistDomain(savedCust)
        custMstHistRepository.save(hist)

        return savedCust
    }

    @Transactional(readOnly = false)
    override suspend fun updateCust(custMstId: String, command: CustUpdateCommand, updater: String): Cust {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")

        cust.update(command, updater)
        val updatedCust = custRepository.save(cust)

        // Save history
        val hist = custMapper.toHistDomain(updatedCust)
        custMstHistRepository.save(hist)

        return updatedCust
    }

    override suspend fun isCustCdExists(custCd: String): Boolean {
        return custCustomRepository.existByCustCd(custCd)
    }

    override fun getCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<CustCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findCustCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toCustCdNmAutoCompleteResponse)
    }

    override fun getRprsCustCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<RprsCustCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findRprsCustCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toRprsCustCdNmAutoCompleteResponse)
    }

    override fun getDirectAcctCdNmAutoCompleteList(searchParam: CustAutoCompleteSearchParam): Flow<DirectAcctCdNmAutoCompleteResponse> {
        val autoCompleteList = custCustomRepository.findDirectAcctCdNmAutoComplete(searchParam)
        return autoCompleteList.map(custMapper::toDirectAcctCdNmAutoCompleteResponse)
    }

    private suspend fun fetchLookupMaps(): LookupMaps {
        val depts = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameById = depts.associate { it.deptCd to it.deptNm }

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userNameById = users.associate { it.userId to it.userNm }

        return LookupMaps(deptNameById, userNameById)
    }

    // 부서명, 사원명, 영업팀코드 치환
    private suspend fun transformCustListResponse(
        custListResponse: CustListResponse,
        deptNameById: Map<String, String>,
        userNameById: Map<String, String>
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

        return custListResponse.copy(deptNm = deptNm, salsPicInfo = transformedSalsPicInfo)
    }

    data class LookupMaps(
        val deptNameById: Map<String, String>,
        val userNameById: Map<String, String>
    )
}
