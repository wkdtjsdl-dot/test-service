package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.SpecimenRegisterRequest
import com.idrsys.ailis.tst.application.dto.SpecimenResponse
import com.idrsys.ailis.tst.application.dto.SpecimenUpdateRequest
import com.idrsys.ailis.tst.application.mapper.SpecimenCommandMapper
import com.idrsys.ailis.tst.application.mapper.SpecimenMapper
import com.idrsys.ailis.tst.application.required.repository.SpecimenRepository
import com.idrsys.ailis.tst.application.usecase.SpecimenUseCase
import com.idrsys.ailis.tst.domain.model.Specimen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.apache.hc.core5.http.HttpStatus
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class SpecimenService(
    private val specimenRepository: SpecimenRepository,
    private val specimenMapper: SpecimenMapper,
    private val commandMapper: SpecimenCommandMapper
) : SpecimenUseCase {

    @Transactional(readOnly = true)
    override suspend fun getSpecimens(spcmNm: String?, spcmCateCd: String?): Flow<SpecimenResponse> {
        return specimenRepository.findAll(spcmNm, spcmCateCd)
            .map { specimenMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override suspend fun getSpecimen(spcmCd: String): SpecimenResponse {
        val specimen = specimenRepository.findById(spcmCd)
            ?: throw NoSuchElementException("Specimen not found: $spcmCd")
        return specimenMapper.toResponse(specimen)
    }

    @Transactional
    override suspend fun registerSpecimen(request: SpecimenRegisterRequest, adminId: String): SpecimenResponse {
        return try {
            val command = commandMapper.toCreateCommand(request)
            val now = LocalDateTime.now()
            val specimen = Specimen.create(command, adminId, now)

            val saved = specimenRepository.save(specimen)
            specimenMapper.toResponse(saved)
        } catch (e: DataIntegrityViolationException) {
            throw ResponseStatusException(HttpStatus.SC_CONFLICT, "중복 데이터 등록 시도로 인한 요청 거절 (Unique Constraint Violation)", e)
        }
    }

    @Transactional
    override suspend fun updateSpecimen(spcmCd: String, request: SpecimenUpdateRequest, adminId: String): SpecimenResponse {
        val specimen = specimenRepository.findById(spcmCd)
            ?: throw NoSuchElementException("Specimen not found: $spcmCd")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        specimen.update(command, adminId, now)

        val saved = specimenRepository.save(specimen)
        return specimenMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteSpecimen(spcmCd: String, adminId: String) {
        val specimen = specimenRepository.findById(spcmCd)
            ?: throw NoSuchElementException("Specimen not found: $spcmCd")

        val now = LocalDateTime.now()
        specimen.delete(updater = adminId, updateDtime = now)

        specimenRepository.save(specimen)
    }
}
