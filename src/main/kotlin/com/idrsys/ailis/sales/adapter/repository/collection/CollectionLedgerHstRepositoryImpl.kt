package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerHstRepository
import com.idrsys.ailis.sales.domain.model.CollectionLedgerHst
import org.springframework.stereotype.Repository

@Repository
class CollectionLedgerHstRepositoryImpl(
    private val collectionLedgerHstDataRepository: CollectionLedgerHstDataRepository
) : CollectionLedgerHstRepository {

    override suspend fun save(collectionLedgerHst: CollectionLedgerHst): CollectionLedgerHst {
        return collectionLedgerHstDataRepository.save(collectionLedgerHst)
    }
}
