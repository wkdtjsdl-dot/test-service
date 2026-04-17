package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeResponse
import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeSearchParam
import kotlinx.coroutines.flow.Flow

interface ClinicalTestCodeRepository {
    fun search(param: ClinicalTestCodeSearchParam): Flow<ClinicalTestCodeResponse>
}
