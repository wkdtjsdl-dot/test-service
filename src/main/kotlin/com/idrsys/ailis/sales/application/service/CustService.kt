package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.cust.CustSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustListResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustCustomRepository
import com.idrsys.ailis.sales.application.usecase.cust.CustUseCase
import com.idrsys.ailis.sales.shared.mapper.CustMapper
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CustService(
    private val custRepository: CustCustomRepository,
    private val custMapper: CustMapper
) : CustUseCase {
    override suspend fun getCustPage(param: CustSearchParam, pageable: Pageable): Page<CustListResponse> {
        val total = custRepository.countCusts(param)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val custs = custRepository.findcustsWithSalsPicInfo(param, pageable).toList()
        val responses = custs.map(custMapper::toResponse)

        return PageImpl(responses, pageable, total)
    }

}