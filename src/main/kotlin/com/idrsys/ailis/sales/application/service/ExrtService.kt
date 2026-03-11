package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtBatchCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtSearchParam
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.ExrtBatchResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtListResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtResponse
import com.idrsys.ailis.sales.application.required.repository.exrt.ExrtCustomRepository
import com.idrsys.ailis.sales.application.required.repository.exrt.ExrtRepository
import com.idrsys.ailis.sales.application.usecase.exrt.ExrtUseCase
import com.idrsys.ailis.sales.domain.model.Exrt
import com.idrsys.ailis.sales.shared.mapper.ExrtMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ExrtService(
    private val exrtRepository: ExrtRepository,
    private val exrtCustomRepository: ExrtCustomRepository,
    private val exrtMapper: ExrtMapper,
) : ExrtUseCase {

    override suspend fun batchRegisterExrt(commands: List<ExrtBatchCommand>): ExrtBatchResponse {
        val totalCount = commands.size

        if (totalCount == 0) {
            return ExrtBatchResponse(
                totalCount = 0,
                insertedCount = 0,
                skippedCount = 0
            )
        }

        val insertedCount = exrtCustomRepository.batchInsert(commands, "BATCH_SYSTEM")
        val skippedCount = totalCount - insertedCount

        return ExrtBatchResponse(
            totalCount = totalCount,
            insertedCount = insertedCount,
            skippedCount = skippedCount
        )
    }

    override suspend fun getExrtPage(searchParam: ExrtSearchParam, pageable: Pageable): Page<ExrtListResponse> {
        val total = exrtCustomRepository.countExrts(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val exrts = exrtCustomRepository.findExrts(searchParam, pageable)
            .map { exrtMapper.toListResponse(it) }
            .toList()

        return PageImpl(exrts, pageable, total)
    }

    override suspend fun getExrtsBy(stndDt: LocalDate?, crcyCd: String?): List<ExrtResponse> {
        val searchParam = ExrtSearchParam(
            stndDtFrom = stndDt,
            stndDtTo = stndDt,
            crcyCd = crcyCd
        )
        return exrtCustomRepository.findExrts(searchParam, null)
            .map { exrtMapper.toResponse(it) }
            .toList()
    }

    override suspend fun getExrtDetail(exrtId: Long): ExrtResponse {
        val exrt = exrtCustomRepository.findExrtById(exrtId)
            ?: throw NoSuchElementException("Exrt not found with id: $exrtId")

        return exrtMapper.toResponse(exrt)
    }

    override suspend fun updateExrt(exrtId: Long, command: ExrtUpdateCommand, adminId: String): ExrtResponse {
        val exrt = exrtCustomRepository.findExrtById(exrtId)
            ?: throw NoSuchElementException("Exrt not found with id: $exrtId")

        exrt.updateStndExrt(command.stndExrt, adminId)

        val updatedExrt = exrtRepository.save(exrt)
        return exrtMapper.toResponse(updatedExrt)
    }

    override suspend fun createExrt(command: ExrtCommand, adminId: String): ExrtResponse {
        // 중복 체크
        val exists = exrtRepository.existsByStndDtAndCrcyCd(command.stndDt, command.crcyCd)
        if (exists) {
            throw IllegalArgumentException("기준일자 ${command.stndDt}, 통화코드 ${command.crcyCd}는 이미 등록되어 있습니다.")
        }

        val now = LocalDateTime.now()
        val exrt = Exrt(
            exrtId = null,
            stndDt = command.stndDt,
            crcyCd = command.crcyCd,
            stndExrt = command.stndExrt,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        ).apply { setAsNew() }

        val saved = exrtRepository.save(exrt)
        return exrtMapper.toResponse(saved)
    }

    override suspend fun deleteExrt(exrtId: Long) {
        // 존재 여부 확인
        exrtCustomRepository.findExrtById(exrtId)
            ?: throw NoSuchElementException("Exrt not found with id: $exrtId")

        exrtRepository.deleteById(exrtId)
    }
}
