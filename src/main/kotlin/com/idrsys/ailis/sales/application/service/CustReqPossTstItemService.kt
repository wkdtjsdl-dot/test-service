package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.required.external.TstServicePort
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemCustomRepository
import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemRepository
import com.idrsys.ailis.sales.application.usecase.custreqposststitem.CustReqPossTstItemUseCase
import com.idrsys.ailis.sales.shared.constant.CustReqPossTstItemErrorCode
import com.idrsys.ailis.sales.shared.mapper.CustReqPossTstItemMapper
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    private val mapper: CustReqPossTstItemMapper,
    private val tstServicePort: TstServicePort
) : CustReqPossTstItemUseCase {

    override suspend fun findItemById(id: Long): CustReqPossTstItemResponse? {
        return repository.findById(id)?.let { mapper.toResponse(it) }
    }

    override suspend fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemResponse> {
        // 1. Repository에서 Query 조회 (tstCd만 포함)
        val items = customRepository.findAllByCustMstId(searchParam).toList()

        // 2. tstCd 리스트 추출
        val tstCds = items.map { it.tstCd }.distinct()

        // 3. TstServiceClient로 검사명 조회
        val tstItems = tstServicePort.findTestItemByTestCode(tstCds) ?: emptyList()
        val tstNmMap = tstItems.associate { it.tstCd to it.tstNm }

        // 4. Query → Response 변환 시 tstNm 추가
        return flow {
            items.forEach { query ->
                val response = mapper.toResponseFromQuery(query)
                val enrichedResponse = response.copy(tstNm = tstNmMap[query.tstCd])
                emit(enrichedResponse)
            }
        }
    }

    override suspend fun getCustReqPossTstItemPage(searchParam: CustReqPossTstItemSearchParam, pageable: Pageable): Page<CustReqPossTstItemResponse> {
        // 1. Repository에서 Query 조회 (tstCd만 포함)
        val items = customRepository.findCustReqPossTstItems(searchParam, pageable).toList()

        // 2. tstCd 리스트 추출
        val tstCds = items.map { it.tstCd }.distinct()

        // 3. TstServiceClient로 검사명 조회
        val tstItems = tstServicePort.findTestItemByTestCode(tstCds) ?: emptyList()
        val tstNmMap = tstItems.associate { it.tstCd to it.tstNm }

        // 4. Query → Response 변환 시 tstNm 추가
        val content = items.map { query ->
            val response = mapper.toResponseFromQuery(query)
            response.copy(tstNm = tstNmMap[query.tstCd])
        }

        val total = customRepository.countCustReqPossTstItems(searchParam)
        return PageImpl(content, pageable, total)
    }

    @Transactional
    override suspend fun saveItem(command: CustReqPossTstItemCommand, creator: String): CustReqPossTstItemResponse {
        // 1. 검사코드 존재 여부 검증 (test-service)
        val tstItems = tstServicePort.findTestItemByTestCode(listOf(command.tstCd))
        if (tstItems.isNullOrEmpty()) {
            throw UserDefinedException(
                CustReqPossTstItemErrorCode.TST_CD_NOT_FOUND_CODE,
                "${CustReqPossTstItemErrorCode.TST_CD_NOT_FOUND_MESSAGE}: ${command.tstCd}"
            )
        }

        // 2. 중복 체크
        val custMstId = command.custMstId ?: throw IllegalArgumentException("custMstId는 필수입니다.")
        val exists = repository.existsByCustMstIdAndTstCd(custMstId, command.tstCd)
        if (exists) {
            throw UserDefinedException(
                CustReqPossTstItemErrorCode.DUPLICATE_CODE,
                "${CustReqPossTstItemErrorCode.DUPLICATE_MESSAGE}: ${command.tstCd}"
            )
        }

        // 3. 저장
        val now = LocalDateTime.now()
        val domain = mapper.toDomain(command, creator, now)
        domain.setAsNew()
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateItem(id: Long, command: CustReqPossTstItemUpdateCommand, updater: String): CustReqPossTstItemResponse {
        // 1. 기존 데이터 조회
        val existing = repository.findById(id)
            ?: throw UserDefinedException(
                CustReqPossTstItemErrorCode.NOT_FOUND_CODE,
                "${CustReqPossTstItemErrorCode.NOT_FOUND_MESSAGE} (ID: $id)"
            )

        // 2. tstCd 변경 시 검증
        if (existing.tstCd != command.tstCd) {
            // 2-1. 검사코드 존재 여부 검증 (test-service)
            val tstItems = tstServicePort.findTestItemByTestCode(listOf(command.tstCd))
            if (tstItems.isNullOrEmpty()) {
                throw UserDefinedException(
                    CustReqPossTstItemErrorCode.TST_CD_NOT_FOUND_CODE,
                    "${CustReqPossTstItemErrorCode.TST_CD_NOT_FOUND_MESSAGE}: ${command.tstCd}"
                )
            }

            // 2-2. 중복 체크
            val custMstId = existing.custMstId ?: throw IllegalArgumentException("custMstId가 null입니다.")
            val exists = repository.existsByCustMstIdAndTstCd(custMstId, command.tstCd)
            if (exists) {
                throw UserDefinedException(
                    CustReqPossTstItemErrorCode.DUPLICATE_CODE,
                    "${CustReqPossTstItemErrorCode.DUPLICATE_MESSAGE}: ${command.tstCd}"
                )
            }
        }

        // 3. 기존 레코드 삭제
        repository.deleteById(id)

        // 4. 새 레코드 생성 (custMstId, custCd는 유지, tstCd는 새 값, creator/createDtime은 현재 시점)
        val now = LocalDateTime.now()
        val newCommand = CustReqPossTstItemCommand(
            custMstId = existing.custMstId,
            custCd = existing.custCd,
            tstCd = command.tstCd
        )
        val newDomain = mapper.toDomain(newCommand, updater, now)
        newDomain.setAsNew()
        val saved = repository.save(newDomain)

        return mapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteCustReqPossTstItem(id: Long) {
        repository.deleteById(id)
    }
}
