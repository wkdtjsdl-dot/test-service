package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.required.repository.collection.CollectionBillHstRepository
import com.idrsys.ailis.sales.domain.model.CollectionBillHst
import org.springframework.stereotype.Repository

@Repository
class CollectionBillHstRepositoryImpl(
    private val collectionBillHstDataRepository: CollectionBillHstDataRepository
) : CollectionBillHstRepository {

    override suspend fun save(collectionBillHst: CollectionBillHst): CollectionBillHst {
        return collectionBillHstDataRepository.save(collectionBillHst)
    }
}
