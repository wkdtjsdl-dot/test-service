package com.idrsys.ailis.sales.application.usecase.custaddinfo

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import kotlinx.coroutines.flow.Flow

interface CustAddInfoUseCase {
    suspend fun findCustAddInfoById(custAddInfoId: Long): CustAddInfoResponse?
    fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoResponse>
    suspend fun createCustAddInfo(command: CustAddInfoCommand, creator: String): CustAddInfoResponse
    suspend fun updateCustAddInfo(custAddInfoId: Long, command: CustAddInfoCommand, updater: String): CustAddInfoResponse
}
