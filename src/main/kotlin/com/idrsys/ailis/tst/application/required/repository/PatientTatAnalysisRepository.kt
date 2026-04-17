package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisResponse
import com.idrsys.ailis.tst.application.dto.PatientTatAnalysisSearchParam
import kotlinx.coroutines.flow.Flow

interface PatientTatAnalysisRepository {
    fun search(param: PatientTatAnalysisSearchParam): Flow<PatientTatAnalysisResponse>
}
