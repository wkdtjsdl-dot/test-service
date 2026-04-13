package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalResponse
import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalSearchParam
import com.idrsys.ailis.tst.application.required.repository.ChargeChangeApprovalRepository
import com.idrsys.ailis.tst.application.usecase.ChargeChangeApprovalUseCase
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChargeChangeApprovalService(
    private val chargeChangeApprovalRepository: ChargeChangeApprovalRepository
) : ChargeChangeApprovalUseCase {

    @Transactional(readOnly = true)
    override suspend fun search(param: ChargeChangeApprovalSearchParam): Flow<ChargeChangeApprovalResponse> {
        return chargeChangeApprovalRepository.search(param)
    }
}
