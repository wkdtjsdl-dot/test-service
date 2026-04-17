package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeResponse
import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeSearchParam
import kotlinx.coroutines.flow.Flow

interface ClinicalTestCodeUseCase {
    suspend fun search(param: ClinicalTestCodeSearchParam): Flow<ClinicalTestCodeResponse>
}
