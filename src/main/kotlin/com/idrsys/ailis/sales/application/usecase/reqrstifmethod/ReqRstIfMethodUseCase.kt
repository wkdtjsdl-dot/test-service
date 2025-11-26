package com.idrsys.ailis.sales.application.usecase.reqrstifmethod

import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodCommand
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import com.idrsys.ailis.sales.application.dto.response.ReqRstIfMethodResponse
import kotlinx.coroutines.flow.Flow

interface ReqRstIfMethodUseCase {
    suspend fun findById(rstIfMethodId: String): ReqRstIfMethodResponse?
    suspend fun findCurrentByCustMstId(custMstId: String): ReqRstIfMethodResponse?
    fun findAllByCustMstId(searchParam: ReqRstIfMethodSearchParam): Flow<ReqRstIfMethodResponse>
    suspend fun save(command: ReqRstIfMethodCommand, creator: String): ReqRstIfMethodResponse
    suspend fun getReqPossYn(custMstId: String): Map<String, Boolean>
    suspend fun updateReqPossYn(custMstId: String, reqPossYn: Boolean, updater: String): Map<String, Boolean>
}
