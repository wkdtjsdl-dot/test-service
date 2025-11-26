package com.idrsys.ailis.sales.application.usecase.custreqposststitem

import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustReqPossTstItemUseCase {
    suspend fun findItemById(id: Long): CustReqPossTstItemResponse?
    fun findAllByCustMstId(searchParam: CustReqPossTstItemSearchParam): Flow<CustReqPossTstItemResponse>
    suspend fun getCustReqPossTstItemPage(searchParam: CustReqPossTstItemSearchParam, pageable: Pageable): Page<CustReqPossTstItemResponse>
    suspend fun saveItem(command: CustReqPossTstItemCommand, creator: String): CustReqPossTstItemResponse
    suspend fun deleteCustReqPossTstItem(id: Long)
}
