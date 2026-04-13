package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisResponse
import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisSearchParam
import com.idrsys.ailis.tst.application.required.repository.PatientTatAnalysisRepository
import com.idrsys.ailis.tst.application.usecase.PatientTatAnalysisUseCase
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PatientTatAnalysisService(
    private val patientTatAnalysisRepository: PatientTatAnalysisRepository
) : PatientTatAnalysisUseCase {

    @Transactional(readOnly = true)
    override suspend fun search(param: PatientTatAnalysisSearchParam): Flow<PatientTatAnalysisResponse> {
        return patientTatAnalysisRepository.search(param)
    }
}
