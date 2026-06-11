package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionBillHst

interface CollectionBillHstRepository {
    suspend fun save(collectionBillHst: CollectionBillHst): CollectionBillHst
}
