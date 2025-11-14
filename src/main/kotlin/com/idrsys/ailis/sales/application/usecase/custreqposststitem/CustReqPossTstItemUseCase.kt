package com.idrsys.ailis.sales.application.usecase.custreqposststitem

import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import kotlinx.coroutines.flow.Flow

interface CustReqPossTstItemUseCase {
    suspend fun findItemById(id: Long): CustReqPossTstItemResponse?
    fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemResponse>
    suspend fun saveItem(command: CustReqPossTstItemCommand, creator: String): CustReqPossTstItemResponse
}
