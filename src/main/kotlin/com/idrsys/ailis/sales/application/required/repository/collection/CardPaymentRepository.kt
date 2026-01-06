package com.idrsys.ailis.sales.application.required.repository.collection

import com.idrsys.ailis.sales.domain.model.CardPayment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Card Payment Repository (Data Interface)
 */
interface CardPaymentRepository : CoroutineCrudRepository<CardPayment, String>
