package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionBill
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Collection Bill Repository (Data Interface)
 */
interface CollectionBillRepository : CoroutineCrudRepository<CollectionBill, String>
