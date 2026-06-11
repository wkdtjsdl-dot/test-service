package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionLedgerHst

interface CollectionLedgerHstRepository {
    suspend fun save(collectionLedgerHst: CollectionLedgerHst): CollectionLedgerHst
}
