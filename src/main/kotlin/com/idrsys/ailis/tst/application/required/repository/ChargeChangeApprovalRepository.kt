package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalResponse
import com.idrsys.ailis.tst.application.dto.ChargeChangeApprovalSearchParam
import kotlinx.coroutines.flow.Flow

interface ChargeChangeApprovalRepository {
    fun search(param: ChargeChangeApprovalSearchParam): Flow<ChargeChangeApprovalResponse>
}
