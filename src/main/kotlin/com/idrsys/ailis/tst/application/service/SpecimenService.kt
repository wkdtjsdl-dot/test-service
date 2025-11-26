package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.SpecimenRegisterRequest
import com.idrsys.ailis.tst.application.dto.SpecimenResponse
import com.idrsys.ailis.tst.application.dto.SpecimenUpdateRequest
import com.idrsys.ailis.tst.application.mapper.SpecimenMapper
import com.idrsys.ailis.tst.application.required.SpecimenRepository
import com.idrsys.ailis.tst.application.usecase.SpecimenUseCase
import com.idrsys.ailis.tst.domain.model.Specimen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SpecimenService(
    private val specimenRepository: SpecimenRepository,
    private val specimenMapper: SpecimenMapper
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
    override suspend fun registerSpecimen(request: SpecimenRegisterRequest): SpecimenResponse {
        val specimen = Specimen(
            spcmCd = request.spcmCd,
            spcmCateCd = request.spcmCateCd,
            useYn = request.useYn,
            spcmNm = request.spcmNm,
            spcmAbbrNm = request.spcmAbbrNm,
            spcmEngNm = request.spcmEngNm,
            spcmEngAbbrNm = request.spcmEngAbbrNm,
            collAmt = request.collAmt,
            engCollAmt = request.engCollAmt,
            spcmStrg = request.spcmStrg,
            engSpcmStrg = request.engSpcmStrg,
            spcmSafe = request.spcmSafe,
            engSpcmSafe = request.engSpcmSafe,
            caution = request.caution,
            engCaution = request.engCaution,
            ref = request.ref,
            engRef = request.engRef,
            creator = "system",
            createDtime = LocalDateTime.now(),
            updater = "system",
            updateDetime = LocalDateTime.now()
        ).apply { setAsNew() }
        
        val saved = specimenRepository.save(specimen)
        return specimenMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun updateSpecimen(spcmCd: String, request: SpecimenUpdateRequest): SpecimenResponse {
        val specimen = specimenRepository.findById(spcmCd)
            ?: throw NoSuchElementException("Specimen not found: $spcmCd")
        
        specimen.update(
            spcmCateCd = request.spcmCateCd,
            useYn = request.useYn,
            spcmNm = request.spcmNm,
            spcmAbbrNm = request.spcmAbbrNm,
            spcmEngNm = request.spcmEngNm,
            spcmEngAbbrNm = request.spcmEngAbbrNm,
            collAmt = request.collAmt,
            engCollAmt = request.engCollAmt,
            spcmStrg = request.spcmStrg,
            engSpcmStrg = request.engSpcmStrg,
            spcmSafe = request.spcmSafe,
            engSpcmSafe = request.engSpcmSafe,
            caution = request.caution,
            engCaution = request.engCaution,
            ref = request.ref,
            engRef = request.engRef,
            updater = "system"
        )
        
        val saved = specimenRepository.save(specimen)
        return specimenMapper.toResponse(saved)
    }

    @Transactional
    override suspend fun deleteSpecimen(spcmCd: String) {
        specimenRepository.deleteById(spcmCd)
    }
}
