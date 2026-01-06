package com.idrsys.ailis.sales.adapter.repository.billing

import com.idrsys.ailis.sales.domain.model.CollectionBill
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * CollectionBillDataRepository
 *
 * R2DBC reactive repository for CollectionBill entity
 */
@Repository
interface CollectionBillDataRepository : CoroutineCrudRepository<CollectionBill, String> {

    /**
     * Find collection bills by customer code
     */
    fun findByCustCd(custCd: String): Flow<CollectionBill>

    /**
     * Find collection bills by date range
     */
    fun findByColbillDtBetween(startDate: LocalDate, endDate: LocalDate): Flow<CollectionBill>

    /**
     * Find collection bills by customer code and date range
     */
    fun findByCustCdAndColbillDtBetween(
        custCd: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<CollectionBill>

    /**
     * Find collection bills by ERP send status
     */
    fun findBySendYn(sendYn: Boolean): Flow<CollectionBill>

    /**
     * Find collection bill by card payment ID
     */
    suspend fun findByCardPayId(cardPayId: String): CollectionBill?

    /**
     * Find collection bill by bank deposit ID
     */
    suspend fun findByBankDepositId(bankDepositId: String): CollectionBill?
}
