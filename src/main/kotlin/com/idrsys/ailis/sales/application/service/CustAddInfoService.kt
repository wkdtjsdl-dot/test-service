package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoRepository
import com.idrsys.ailis.sales.application.usecase.custaddinfo.CustAddInfoUseCase
import com.idrsys.ailis.sales.shared.mapper.CustAddInfoMapper
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
class CustAddInfoService(
    private val custAddInfoRepository: CustAddInfoRepository,
    private val custAddInfoCustomRepository: CustAddInfoCustomRepository,
    private val custAddInfoMapper: CustAddInfoMapper
) : CustAddInfoUseCase {

    override suspend fun getCustAddInfoPage(searchParam: CustAddInfoSearchParam, pageable: Pageable): Page<CustAddInfoResponse> {
        val total = custAddInfoCustomRepository.countCustAddInfos(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custAddInfos = custAddInfoCustomRepository.findCustAddInfos(searchParam, pageable)
            .map(custAddInfoMapper::toResponseFromQuery)
            .toList()

        return PageImpl(custAddInfos, pageable, total)
    }

    override suspend fun getCustAddInfoDetail(custAddInfoId: Long): CustAddInfoResponse {
        val dto = custAddInfoCustomRepository.findCustAddInfoById(custAddInfoId)
            ?: throw NoSuchElementException("CustAddInfo not found with id: $custAddInfoId")

        return custAddInfoMapper.toResponseFromQuery(dto)
    }

    override suspend fun getCustAddInfoDetailByCustMstId(custMstId: String): CustAddInfoResponse {
        val dto = custAddInfoCustomRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("CustAddInfo not found with custMstId: $custMstId")

        return custAddInfoMapper.toResponseFromQuery(dto)
    }

    override suspend fun findCustAddInfoByCustMstId(custMstId: String): CustAddInfoResponse? {
        return custAddInfoCustomRepository.findByCustMstId(custMstId)?.let {
            custAddInfoMapper.toResponseFromQuery(it)
        }
    }

    override fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoResponse> {
        return custAddInfoCustomRepository.findAllByCustMstId(searchParam)
            .map(custAddInfoMapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun createCustAddInfo(custMstId: String, command: CustAddInfoCommand, creator: String): CustAddInfoResponse {
        val now = LocalDateTime.now()
        val custAddInfo = custAddInfoMapper.toDomain(command, creator, now).apply { setAsNew() }
        val savedCustAddInfo = custAddInfoRepository.save(custAddInfo)
        return custAddInfoMapper.toResponse(savedCustAddInfo)
    }

    @Transactional
    override suspend fun updateCustAddInfo(custMstId: String, custAddInfoId: Long, command: CustAddInfoCommand, updater: String): CustAddInfoResponse {
        val custAddInfo = custAddInfoCustomRepository.findDomainById(custAddInfoId)
            ?: throw NoSuchElementException("CustAddInfo not found with id: $custAddInfoId")

        custAddInfo.update(command, updater)

        val updatedCustAddInfo = custAddInfoRepository.save(custAddInfo)
        return custAddInfoMapper.toResponse(updatedCustAddInfo)
    }

    @Transactional
    override suspend fun updateCustAddInfoByCustMstId(custMstId: String, command: CustAddInfoCommand, updater: String): CustAddInfoResponse {
        val custAddInfo = custAddInfoCustomRepository.findDomainByCustMstId(custMstId)
            ?: throw NoSuchElementException("CustAddInfo not found with custMstId: $custMstId")

        custAddInfo.update(command, updater)

        val updatedCustAddInfo = custAddInfoRepository.save(custAddInfo)
        return custAddInfoMapper.toResponse(updatedCustAddInfo)
    }

    @Transactional
    override suspend fun deleteCustAddInfo(custAddInfoId: Long) {
        val custAddInfo = custAddInfoRepository.findById(custAddInfoId)
            ?: throw NoSuchElementException("CustAddInfo not found with id: $custAddInfoId")

        custAddInfoRepository.delete(custAddInfo)
    }

    @Transactional
    override suspend fun deleteCustAddInfoByCustMstId(custMstId: String) {
        val custAddInfo = custAddInfoCustomRepository.findDomainByCustMstId(custMstId)
            ?: throw NoSuchElementException("CustAddInfo not found with custMstId: $custMstId")

        custAddInfoRepository.delete(custAddInfo)
    }
}
