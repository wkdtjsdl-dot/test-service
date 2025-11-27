package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.adapter.external.BaseServiceClient
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeRegisterCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeUpdateCommand
import com.idrsys.ailis.sales.application.dto.request.charge.ChargeSearchParam
import com.idrsys.ailis.sales.application.dto.response.ChargeResponse
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeCustomRepository
import com.idrsys.ailis.sales.application.required.repository.charge.ChargeRepository // chargeRepository는 save에만 사용되므로 유지
import com.idrsys.ailis.sales.application.usecase.charge.ChargeUseCase
import com.idrsys.ailis.sales.shared.mapper.ChargeMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.NoSuchElementException
import java.util.UUID

@Service
class ChargeService(
    private val chargeCustomRepository: ChargeCustomRepository,
    private val chargeMapper: ChargeMapper,
    private val baseServiceClient: BaseServiceClient,
    private val chargeRepository: ChargeRepository,
) : ChargeUseCase {

    override suspend fun getChargePage(
        searchParam: ChargeSearchParam,
        pageable: Pageable
    ): Page<ChargeResponse> {

        var finalSearchParam = searchParam

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        val total = chargeCustomRepository.countCharge(finalSearchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val charges: List<ChargeResponse> = chargeCustomRepository.findCharges(finalSearchParam, pageable)
            .map(chargeMapper::toResponse)
            .toList()

        val chargeResponses = charges.map { charge ->
            val updatedSalesPics = charge.salesPics?.map { pic ->
                pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
            }
            charge.copy(
                bzoffiNm = deptNameByCd[charge.bzoffiCd],
                salesPics = updatedSalesPics
            )
        }

        return PageImpl(chargeResponses, pageable, total)
    }
    
    override suspend fun getCharges(searchParam: ChargeSearchParam): List<ChargeResponse> {
        var finalSearchParam = searchParam

        val users = baseServiceClient.getUsers() ?: emptyList()
        val userMap = users.associateBy { it.userId }

        if (!searchParam.empUserIdNm.isNullOrBlank()) {
            val keyword = searchParam.empUserIdNm
            val userIds = userMap.values.filter { it.userNm.contains(keyword, ignoreCase = true) || it.userId.contains(keyword, ignoreCase = true) }
                .map { it.userId }
                .toList()
            finalSearchParam = searchParam.copy(empUserIds = userIds)
        }

        val departments = baseServiceClient.getDepartments() ?: emptyList()
        val deptNameByCd = departments.associate { it.deptCd to it.deptNm }

        return chargeCustomRepository.findCharges(finalSearchParam, Pageable.unpaged())
            .map(chargeMapper::toResponse)
            .map { charge ->
                val updatedSalesPics = charge.salesPics?.map { pic ->
                    pic.copy(empUserNm = userMap[pic.empUserId]?.userNm)
                }
                charge.copy(bzoffiNm = deptNameByCd[charge.bzoffiCd], salesPics = updatedSalesPics)
            }.toList()
    }
    

    @Transactional
    override suspend fun registerCharge(command: ChargeRegisterCommand, creator: String): ChargeResponse {
        val now = LocalDateTime.now()
        val newChargeId = UUID.randomUUID().toString()
        val newCharge = chargeMapper.toDomain(command, newChargeId, creator, now).apply { setAsNew() }
        val savedCharge = chargeRepository.save(newCharge)
        return chargeMapper.toResponse(savedCharge)
    }

    @Transactional
    override suspend fun updateCharge(custChargeId: String, command: ChargeUpdateCommand, updater: String): ChargeResponse {
        val charge = chargeRepository.findById(custChargeId)
            ?: throw NoSuchElementException("수정할 고객 수가 정보를 찾을 수 없습니다: $custChargeId")

        charge.update(command, updater)

        val updatedCharge = chargeRepository.save(charge)
        return chargeMapper.toResponse(updatedCharge)
    }

    override suspend fun getCharge(custChargeId: String): ChargeResponse {
        val chargeWithDetails = chargeCustomRepository.findChargeWithDetailsById(custChargeId)
            ?: throw NoSuchElementException("고객 수가 정보를 찾을 수 없습니다: $custChargeId")

        return chargeMapper.toResponse(chargeWithDetails)
    }
}
