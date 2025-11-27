package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.SpecimenContainerRegisterRequest
import com.idrsys.ailis.tst.application.dto.SpecimenContainerResponse
import com.idrsys.ailis.tst.application.dto.SpecimenContainerUpdateRequest
import com.idrsys.ailis.tst.application.mapper.SpecimenContainerMapper
import com.idrsys.ailis.tst.application.required.SpecimenContainerRepository
import com.idrsys.ailis.tst.application.usecase.SpecimenContainerUseCase
import com.idrsys.ailis.tst.domain.model.SpecimenContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SpecimenContainerService(
    private val specimenContainerRepository: SpecimenContainerRepository,
    private val specimenContainerMapper: SpecimenContainerMapper
) : SpecimenContainerUseCase {

    @Transactional(readOnly = true)
    override suspend fun getContainers(cntnNm: String?): Flow<SpecimenContainerResponse> {
        return specimenContainerRepository.findAllByCntnNm(cntnNm)
            .map { specimenContainerMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override suspend fun getContainer(spcmCntnCd: String): SpecimenContainerResponse {
        val container = specimenContainerRepository.findById(spcmCntnCd)
            ?: throw NoSuchElementException("Container not found: $spcmCntnCd")
        return specimenContainerMapper.toResponse(container)
    }

    @Transactional
    override suspend fun registerContainer(request: SpecimenContainerRegisterRequest, adminId: String): SpecimenContainerResponse {
        val container = SpecimenContainer(
            spcmCntnCd = request.spcmCntnCd,
            cntnNm = request.cntnNm,
            cntnEngNm = request.cntnEngNm,
            cntnFileId = request.cntnFileId,
            creator = adminId,
            createDtime = LocalDateTime.now(),
            updater = adminId,
            updateDetime = LocalDateTime.now()
        ).apply { setAsNew() }
        
        val saved = specimenContainerRepository.save(container)
        return specimenContainerMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateContainer(spcmCntnCd: String, request: SpecimenContainerUpdateRequest, adminId: String): SpecimenContainerResponse {
        val container = specimenContainerRepository.findById(spcmCntnCd)
            ?: throw NoSuchElementException("Container not found: $spcmCntnCd")
        
        container.update(
            cntnNm = request.cntnNm,
            cntnEngNm = request.cntnEngNm,
            cntnFileId = request.cntnFileId,
            updater = adminId
        )
        
        val saved = specimenContainerRepository.save(container)
        return specimenContainerMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteContainer(spcmCntnCd: String, adminId: String) {
        specimenContainerRepository.deleteById(spcmCntnCd)
    }
}
