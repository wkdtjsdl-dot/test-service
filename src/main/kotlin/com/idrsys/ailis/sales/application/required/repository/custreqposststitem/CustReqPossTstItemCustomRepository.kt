package com.idrsys.ailis.sales.application.required.repository.custreqposststitem

import com.idrsys.ailis.sales.application.dto.query.CustReqPossTstItemQuery
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CustReqPossTstItemCustomRepository {
    fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemQuery>
    fun findCustReqPossTstItems(searchParam: CustReqPossTstItemSearchParam, pageable: Pageable?): Flow<CustReqPossTstItemQuery>
    suspend fun countCustReqPossTstItems(searchParam: CustReqPossTstItemSearchParam): Long
}
