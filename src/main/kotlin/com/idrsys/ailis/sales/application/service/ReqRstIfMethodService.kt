package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import com.idrsys.ailis.sales.application.dto.response.ReqRstIfMethodResponse
import com.idrsys.ailis.sales.application.required.repository.cust.CustRepository
import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodCustomRepository
import com.idrsys.ailis.sales.application.required.repository.reqrstifmethod.ReqRstIfMethodRepository
import com.idrsys.ailis.sales.application.usecase.reqrstifmethod.ReqRstIfMethodUseCase
import com.idrsys.ailis.sales.shared.mapper.ReqRstIfMethodMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReqRstIfMethodService(
    private val repository: ReqRstIfMethodRepository,
    private val customRepository: ReqRstIfMethodCustomRepository,
    private val mapper: ReqRstIfMethodMapper,
    private val custRepository: CustRepository
) : ReqRstIfMethodUseCase {

    override suspend fun findById(rstIfMethodId: String): ReqRstIfMethodResponse? {
        return repository.findById(rstIfMethodId)?.let { mapper.toResponse(it) }
    }

    override suspend fun findCurrentByCustMstId(custMstId: String): ReqRstIfMethodResponse? {
        return customRepository.findCurrentByCustMstId(custMstId)?.let { mapper.toResponse(it) }
    }

    override fun findAllByCustMstId(searchParam: ReqRstIfMethodSearchParam): Flow<ReqRstIfMethodResponse> {
        return customRepository.findAllByCustMstId(searchParam)
            .map(mapper::toResponseFromQuery)
    }

    @Transactional
    override suspend fun save(command: ReqRstIfMethodCommand, creator: String): ReqRstIfMethodResponse {
        val custMstId = command.custMstId ?: throw IllegalArgumentException("custMstId is required")
        val today = java.time.LocalDate.now()
        val yesterday = today.minusDays(1)
        val now = LocalDateTime.now()

        // 1. 현재 유효한 레코드 조회
        val currentRecord = customRepository.findCurrentByCustMstId(custMstId)

        return if (currentRecord == null) {
            // 2. 기존 레코드 없으면: 새 레코드 생성
            val commandWithDates = command.copy(
                reqRstIfMethodId = command.reqRstIfMethodId ?: java.util.UUID.randomUUID().toString(),
                applyStartDt = today,
                applyEndDt = java.time.LocalDate.parse("9999-12-31")
            )
            val domain = mapper.toDomain(commandWithDates, creator, now)
            domain.setAsNew()
            val saved = repository.save(domain)
            mapper.toResponse(saved)
        } else {
            // 3. 기존 레코드 있으면
            val isChanged = currentRecord.reqMethodCd != command.reqMethodCd ||
                           currentRecord.reqIfTypeCd != command.reqIfTypeCd

            if (!isChanged) {
                // 3-a. 값 안 바뀜: no-op, 기존 레코드 반환
                mapper.toResponse(currentRecord)
            } else {
                // 3-b. 값 바뀜
                if (currentRecord.applyStartDt == today) {
                    // 당일 수정: 덮어쓰기 (UPDATE)
                    currentRecord.update(command.copy(
                        applyStartDt = today,
                        applyEndDt = java.time.LocalDate.parse("9999-12-31")
                    ), creator)
                    val saved = repository.save(currentRecord)
                    mapper.toResponse(saved)
                } else {
                    // 이전 날짜: 이력 생성
                    // 기존 레코드 종료일 업데이트
                    currentRecord.updateEndDate(yesterday, creator)
                    repository.save(currentRecord)

                    // 새 레코드 생성
                    val commandWithDates = command.copy(
                        reqRstIfMethodId = java.util.UUID.randomUUID().toString(),
                        applyStartDt = today,
                        applyEndDt = java.time.LocalDate.parse("9999-12-31")
                    )
                    val newDomain = mapper.toDomain(commandWithDates, creator, now)
                    newDomain.setAsNew()
                    val saved = repository.save(newDomain)
                    mapper.toResponse(saved)
                }
            }
        }
    }

    override suspend fun getReqPossYn(custMstId: String): Map<String, Boolean> {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")
        return mapOf("reqPossYn" to cust.reqPossYn)
    }

    @Transactional
    override suspend fun updateReqPossYn(custMstId: String, reqPossYn: Boolean, updater: String): Map<String, Boolean> {
        val cust = custRepository.findByCustMstId(custMstId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다: $custMstId")

        cust.updateReqPossYn(reqPossYn, updater)
        custRepository.save(cust)

        return mapOf("reqPossYn" to reqPossYn)
    }
}
