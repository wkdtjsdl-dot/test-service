package com.idrsys.ailis.sales.application.usecase.custaddinfo

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustAddInfoUseCase {
    suspend fun getCustAddInfoPage(searchParam: CustAddInfoSearchParam, pageable: Pageable): Page<CustAddInfoResponse>
    suspend fun getCustAddInfoDetail(custAddInfoId: Long): CustAddInfoResponse
    suspend fun getCustAddInfoDetailByCustMstId(custMstId: String): CustAddInfoResponse
    suspend fun findCustAddInfoByCustMstId(custMstId: String): CustAddInfoResponse?
    fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoResponse>
    suspend fun createCustAddInfo(custMstId: String, command: CustAddInfoCommand, creator: String): CustAddInfoResponse
    suspend fun updateCustAddInfo(custMstId: String, custAddInfoId: Long, command: CustAddInfoCommand, updater: String): CustAddInfoResponse
    suspend fun updateCustAddInfoByCustMstId(custMstId: String, command: CustAddInfoCommand, updater: String): CustAddInfoResponse
    suspend fun deleteCustAddInfo(custAddInfoId: Long)
    suspend fun deleteCustAddInfoByCustMstId(custMstId: String)
}
