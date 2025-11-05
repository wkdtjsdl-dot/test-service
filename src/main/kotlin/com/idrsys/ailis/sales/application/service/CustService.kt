package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.dto.response.CustResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.shared.mapper.CustMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CustService(
    private val custCustomRepository: CustCustomRepository,
    private val custRepository: CustRepository,
    private val custMapper: CustMapper
) : CustUseCase {
    override suspend fun getCustPage(searchParam: CustSearchParam, pageable: Pageable): Page<CustListResponse> {
        val total = custCustomRepository.countCusts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custs = custCustomRepository.findCustsWithSalsPicInfo(searchParam, pageable).toList()
        val responses = custs.map(custMapper::toListResponse)

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

}