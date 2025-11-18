package com.idrsys.ailis.sales.adapter.repository.custreqposststitem

import com.idrsys.ailis.sales.application.required.repository.custreqposststitem.CustReqPossTstItemRepository
import com.idrsys.ailis.sales.domain.model.CustReqPossTstItem
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustReqPossTstItemDataRepository : CoroutineCrudRepository<CustReqPossTstItem, Long>

@Repository
class CustReqPossTstItemRepositoryImpl(
    private val dataRepository: CustReqPossTstItemDataRepository
) : CustReqPossTstItemRepository {

    override suspend fun findById(id: Long): CustReqPossTstItem? {
        return dataRepository.findById(id)
    }

    override suspend fun save(custReqPossTstItem: CustReqPossTstItem): CustReqPossTstItem {
        return dataRepository.save(custReqPossTstItem)
    }
}
