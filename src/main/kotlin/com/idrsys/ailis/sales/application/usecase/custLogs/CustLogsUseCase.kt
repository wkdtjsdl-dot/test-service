package com.idrsys.ailis.sales.application.usecase.custLogs

import com.idrsys.ailis.sales.application.dto.response.CustDetailLogs
import com.idrsys.ailis.sales.application.dto.response.CustLogsEditResponse
import com.idrsys.ailis.sales.application.dto.response.CustLogsSearchParam
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.domain.model.CustMstHst

interface CustLogsUseCase {
    suspend fun getCustomerLogList(searchParam: CustLogsSearchParam): List<CustLogsEditResponse>

    suspend fun getCustomerLogDetail(logId: String): List<CustDetailLogs>

    suspend fun getCustomerLogRecover(logId: String): CustMstHst

    suspend fun putCustomerLogRecover(logId: Long, adminId: String): Cust
}
