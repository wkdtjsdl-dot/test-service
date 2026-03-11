package com.idrsys.ailis.sales.application.required.repository.ifCustInfo

import com.idrsys.ailis.sales.application.dto.query.IfCustInfoQuery
import com.idrsys.ailis.sales.application.dto.request.ifCustInfo.IfCustInfoSearchParam
import com.idrsys.ailis.sales.domain.model.IfCustInfo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface IfCustInfoCustomRepository {
    fun findIfCustInfos(searchParam: IfCustInfoSearchParam, pageable: Pageable?): Flow<IfCustInfoQuery>
    suspend fun countIfCustInfos(searchParam: IfCustInfoSearchParam): Long
    suspend fun findIfCustInfoById(ifCustInfoId: String): IfCustInfoQuery?
    suspend fun findDomainById(id: String): IfCustInfo?
    suspend fun findByCustMstId(custMstId: String): IfCustInfo?
}
