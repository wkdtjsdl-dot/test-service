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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustAddInfoService(
    private val custAddInfoRepository: CustAddInfoRepository,
    private val custAddInfoCustomRepository: CustAddInfoCustomRepository,
    private val custAddInfoMapper: CustAddInfoMapper
) : CustAddInfoUseCase {

    override suspend fun findCustAddInfoById(custAddInfoId: Long): CustAddInfoResponse? {
        return custAddInfoRepository.findById(custAddInfoId)?.let {
            custAddInfoMapper.toResponse(it)
        }
    }

    override fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoResponse> {
        return custAddInfoCustomRepository.findAllByCustMstId(searchParam)
            .map(custAddInfoMapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun createCustAddInfo(command: CustAddInfoCommand, creator: String): CustAddInfoResponse {
        val now = LocalDateTime.now()
        val custAddInfo = custAddInfoMapper.toDomain(command, creator, now).apply { setAsNew() }
        val savedCustAddInfo = custAddInfoRepository.save(custAddInfo)
        return custAddInfoMapper.toResponse(savedCustAddInfo)
    }

    @Transactional
    override suspend fun updateCustAddInfo(custAddInfoId: Long, command: CustAddInfoCommand, updater: String): CustAddInfoResponse {
        val custAddInfo = custAddInfoRepository.findById(custAddInfoId)
            ?: throw NoSuchElementException("CustAddInfo not found with id: $custAddInfoId")

        custAddInfo.update(command, updater)

        val updatedCustAddInfo = custAddInfoRepository.save(custAddInfo)
        return custAddInfoMapper.toResponse(updatedCustAddInfo)
    }
}
