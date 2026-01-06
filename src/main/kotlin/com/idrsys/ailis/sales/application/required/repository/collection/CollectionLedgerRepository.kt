package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CollectionLedger
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Collection Ledger Repository (Data Interface)
 */
interface CollectionLedgerRepository : CoroutineCrudRepository<CollectionLedger, String> {
    fun findByCustCdAndColbillDtBetweenOrderByColbillDtAsc(
        custCd: String,
        startDt: java.time.LocalDate,
        endDt: java.time.LocalDate
    ): Flow<CollectionLedger>
}
