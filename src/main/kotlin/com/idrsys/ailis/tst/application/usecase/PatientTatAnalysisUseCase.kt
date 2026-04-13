package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisResponse
import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisSearchParam
import kotlinx.coroutines.flow.Flow

interface PatientTatAnalysisUseCase {
    suspend fun search(param: PatientTatAnalysisSearchParam): Flow<PatientTatAnalysisResponse>
}
