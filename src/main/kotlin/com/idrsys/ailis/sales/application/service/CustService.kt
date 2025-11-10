package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.cust.CustRegisterCommand
import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.cust.CustUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.shared.mapper.CustMapper
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val custMapper: CustMapper,
    private val baseServiceClient: BaseServiceClient
) : CustUseCase {
    override suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> {
        val total = custCustomRepository.countCusts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custs = custCustomRepository.findCustsWithSalsPicInfo(searchParam, pageable).toList()

        val deptIds = custs.mapNotNull { it.bzoffiCd }.filter { it.isNotBlank() }.distinct()

        val deptsMap = if (deptIds.isNotEmpty()) {
            kotlinx.coroutines.coroutineScope {
                deptIds.map { id ->
                    async {
                        try {
                            baseServiceClient.findDepartmentById(id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull().associateBy { it.deptCd }
            }
        } else {
            emptyMap()
        }

        val responses = custs.map { cust ->
            val response = custMapper.toListResponse(cust)
            val dept = deptsMap[cust.bzoffiCd]
            response.copy(deptNm = dept?.deptNm)
        }

        return PageImpl(responses, pageable, total)
    }

    override fun getCusts(searchParam: CustSearchParam): Flow<CustListResponse> {
        val custs = custCustomRepository.findCustsWithSalsPicInfo(searchParam,null)
        return custs.map(custMapper::toListResponse)
    }

    override suspend fun findCustByCustMstId(custMstId: String): CustResponse {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")
        return custMapper.toDetailResponse(cust)
    }

    @Transactional(readOnly = false)
    override suspend fun registerCust(command: CustRegisterCommand, creator: String): Cust {
        val custCd = command.custCd

        if (custCustomRepository.existByCustCd(custCd)) {
            throw UserDefinedException(
                "USER_ALREADY_EXIST", "이미 존재하는 고객코드입니다 : $custCd")
        }

        val newCust = custMapper.toDomain(command, creator, LocalDateTime.now())

        return custRepository.save(newCust)
    }

    override suspend fun updateCust(custMstId: String, command: CustUpdateCommand, updater: String): Cust {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")

        cust.update(command, updater)

        return custRepository.save(cust)
    }

    override suspend fun isCustCdExists(custCd: String): Boolean {
        return custCustomRepository.existByCustCd(custCd)
    }

}