package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemRepository
import com.idrsys.ailis.sales.application.usecase.custreqposststitem.CustReqPossTstItemUseCase
import com.idrsys.ailis.sales.shared.mapper.CustReqPossTstItemMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustReqPossTstItemService(
    private val repository: CustReqPossTstItemRepository,
    private val customRepository: CustReqPossTstItemCustomRepository,
    private val mapper: CustReqPossTstItemMapper
) : CustReqPossTstItemUseCase {

    override suspend fun findItemById(id: Long): CustReqPossTstItemResponse? {
        return repository.findById(id)?.let { mapper.toResponse(it) }
    }

    override fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemResponse> {
        return customRepository.findAllByCustMstId(searchParam)
            .map(mapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun saveItem(command: CustReqPossTstItemCommand, creator: String): CustReqPossTstItemResponse {
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(command, creator, now)
        domain.setAsNew()
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }
}
