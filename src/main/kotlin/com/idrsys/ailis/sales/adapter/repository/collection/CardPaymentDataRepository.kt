package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.domain.model.CardPayment
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * CardPaymentDataRepository
 *
 * R2DBC reactive repository for CardPayment entity
 */
@Repository
interface CardPaymentDataRepository : CoroutineCrudRepository<CardPayment, String> {

    /**
     * Find card payments by registration status
     */
    fun findByRegYn(regYn: Boolean): Flow<CardPayment>

    /**
     * Find card payments by payment division code (10: approved, 20: cancelled)
     */
    fun findByPayDivCd(payDivCd: String): Flow<CardPayment>

    /**
     * Find card payments by registration status and payment date range
     */
    fun findByRegYnAndPayDtBetween(
        regYn: Boolean,
        startDate: String,
        endDate: String
    ): Flow<CardPayment>

    /**
     * Find card payments by shop ID
     */
    fun findByShopId(shopId: String): Flow<CardPayment>
}