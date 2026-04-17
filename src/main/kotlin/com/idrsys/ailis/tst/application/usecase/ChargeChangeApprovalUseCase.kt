package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalResponse
import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalSearchParam
import kotlinx.coroutines.flow.Flow

interface ChargeChangeApprovalUseCase {
    suspend fun search(param: ChargeChangeApprovalSearchParam): Flow<ChargeChangeApprovalResponse>
}
