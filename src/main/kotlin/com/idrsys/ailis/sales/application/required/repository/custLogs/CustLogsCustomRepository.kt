package com.idrsys.ailis.sales.application.required.repository.custLogs

import com.idrsys.ailis.sales.domain.model.CustMstHst
import kotlinx.coroutines.flow.Flow

interface CustLogsCustomRepository {
    suspend fun findAllByCustMstId(custMstId: String): Flow<CustMstHst>

    suspend fun findDiffCustLogByCustMstHstId(custMstHstId: Long): Flow<CustMstHst>
}