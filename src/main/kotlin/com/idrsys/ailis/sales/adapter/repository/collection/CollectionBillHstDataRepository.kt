package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionBillHst
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CollectionBillHstDataRepository : CoroutineCrudRepository<CollectionBillHst, String>
