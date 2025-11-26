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
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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

    override suspend fun getCustReqPossTstItemPage(searchParam: CustReqPossTstItemSearchParam, pageable: Pageable): Page<CustReqPossTstItemResponse> {
        val content = customRepository.findCustReqPossTstItems(searchParam, pageable)
            .map(mapper::toResponseFromQuery)
            .toList()
        val total = customRepository.countCustReqPossTstItems(searchParam)
        return PageImpl(content, pageable, total)
    }

    @Transactional
    override suspend fun saveItem(command: CustReqPossTstItemCommand, creator: String): CustReqPossTstItemResponse {
        // 중복 체크
        val custMstId = command.custMstId ?: throw IllegalArgumentException("custMstId는 필수입니다.")
        val exists = repository.existsByCustMstIdAndTstCd(custMstId, command.tstCd)
        if (exists) {
            throw IllegalArgumentException("검사코드 ${command.tstCd}는 이미 등록되어 있습니다.")
        }

        val now = LocalDateTime.now()
        val domain = mapper.toDomain(command, creator, now)
        domain.setAsNew()
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteCustReqPossTstItem(id: Long) {
        repository.deleteById(id)
    }
}
