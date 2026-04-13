package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeResponse
import com.idrsys.ailis.tst.application.dto.ClinicalTestCodeSearchParam
import com.idrsys.ailis.tst.application.required.repository.ClinicalTestCodeRepository
import com.idrsys.ailis.tst.application.usecase.ClinicalTestCodeUseCase
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClinicalTestCodeService(
    private val clinicalTestCodeRepository: ClinicalTestCodeRepository
) : ClinicalTestCodeUseCase {

    @Transactional(readOnly = true)
    override suspend fun search(param: ClinicalTestCodeSearchParam): Flow<ClinicalTestCodeResponse> {
        return clinicalTestCodeRepository.search(param)
    }
}
