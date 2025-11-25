package com.idrsys.ailis.sales.application.required.repository.custLogs

import com.idrsys.ailis.sales.domain.model.CustMstHst

interface CustLogsRepository {
    suspend fun save(custLogs: CustMstHst): CustMstHst
    suspend fun findById(id: Long): CustMstHst?
}