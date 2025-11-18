package com.idrsys.ailis.sales.application.required.repository.custreqposststitem

import com.idrsys.ailis.sales.domain.model.CustReqPossTstItem

interface CustReqPossTstItemRepository {
    suspend fun findById(id: Long): CustReqPossTstItem?
    suspend fun save(custReqPossTstItem: CustReqPossTstItem): CustReqPossTstItem
}
