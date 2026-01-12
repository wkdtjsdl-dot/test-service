package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.required.repository.collection.CardPaymentRepository
import com.idrsys.ailis.sales.domain.model.CardPayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jooq.DSLContext
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class CardPaymentRepositoryImpl(
    private val cardPaymentDataRepository: CardPaymentDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : CardPaymentRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(cardPayment: CardPayment): CardPayment {
        return cardPaymentDataRepository.save(cardPayment)
    }

    override suspend fun findById(id: String): CardPayment? {
        return cardPaymentDataRepository.findById(id)
    }

    override suspend fun delete(cardPayment: CardPayment) {
        cardPaymentDataRepository.delete(cardPayment)
    }

    // Custom query operations (implemented with jOOQ)
    override fun findCardPayments(searchParam: CardPaymentSearchParam): Flow<CardPayment> {
        // TODO: Implement with jOOQ when needed
        return emptyFlow()
    }

    override suspend fun findCardPaymentById(cardPayId: String): CardPayment? {
        return cardPaymentDataRepository.findById(cardPayId)
    }

    override suspend fun updateRegYn(cardPayId: String, regYn: Boolean, updater: String) {
        // TODO: Implement with jOOQ when needed
    }
}