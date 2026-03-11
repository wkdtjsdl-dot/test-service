package com.idrsys.ailis.sales.application.required.repository.custaddinfo

import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.domain.model.CustAddInfo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustAddInfoCustomRepository {
    fun findCustAddInfos(searchParam: CustAddInfoSearchParam, pageable: Pageable?): Flow<CustAddInfoQuery>
    suspend fun countCustAddInfos(searchParam: CustAddInfoSearchParam): Long
    suspend fun findCustAddInfoById(custAddInfoId: Long): CustAddInfoQuery?
    suspend fun findDomainById(id: Long): CustAddInfo?
    suspend fun findByCustMstId(custMstId: String): CustAddInfoQuery?
    suspend fun findDomainByCustMstId(custMstId: String): CustAddInfo?
    fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoQuery>
}
