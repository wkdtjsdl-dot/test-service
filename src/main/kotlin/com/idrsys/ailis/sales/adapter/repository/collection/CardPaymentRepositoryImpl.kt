package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.request.collection.CardPaymentSearchParam
import com.idrsys.ailis.sales.application.required.repository.collection.CardPaymentRepository
import com.idrsys.ailis.sales.domain.model.CardPayment
import com.idrsys.ailis.sales.generated.jooq.tables.SblCardPay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

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
        val table = SblCardPay.SBL_CARD_PAY
        var condition = DSL.noCondition()

        condition = condition.and(table.PAY_DT.ge(searchParam.startDt))
        condition = condition.and(table.PAY_DT.le(searchParam.endDt))
        if (!searchParam.payDivCd.isNullOrBlank()) {
            condition = condition.and(table.PAY_DIV_CD.eq(searchParam.payDivCd))
        }
        searchParam.regYn?.let {
            condition = condition.and(table.REG_YN.eq(it))
        }

        val query = dslContext.selectFrom(table).where(condition)

        var executeSpec = databaseClient.sql(query.sql)
        query.bindValues.forEachIndexed { index, value ->
            executeSpec = executeSpec.bind(index, value)
        }

        return executeSpec
            .fetch()
            .all()
            .map { row ->
                CardPayment(
                    cardPayId = row[table.CARD_PAY_ID.name] as String,
                    shopId = row[table.SHOP_ID.name] as String,
                    tradeNo = row[table.TRADE_NO.name] as String,
                    cardBillNo = row[table.CARD_BILL_NO.name] as String?,
                    cardCompCd = row[table.CARD_COMP_CD.name] as String?,
                    cardCompNm = row[table.CARD_COMP_NM.name] as String?,
                    cardNo = row[table.CARD_NO.name] as String?,
                    instlMonth = row[table.INSTL_MONTH.name] as String?,
                    payAmt = row[table.PAY_AMT.name] as BigDecimal,
                    payDt = row[table.PAY_DT.name] as String,
                    payTime = row[table.PAY_TIME.name] as String?,
                    cardApprNo = row[table.CARD_APPR_NO.name] as String?,
                    payDivCd = row[table.PAY_DIV_CD.name] as String,
                    regYn = row[table.REG_YN.name] as Boolean,
                    creator = row[table.CREATOR.name] as String,
                    createDtime = row[table.CREATE_DTIME.name] as LocalDateTime,
                    updater = row[table.UPDATER.name] as String,
                    updateDtime = row[table.UPDATE_DTIME.name] as LocalDateTime,
                    outamt = row[table.OUTAMT.name] as BigDecimal
                )
            }
            .asFlow()
    }

    override suspend fun findCardPaymentById(cardPayId: String): CardPayment? {
        return cardPaymentDataRepository.findById(cardPayId)
    }

    override suspend fun updateRegYn(cardPayId: String, regYn: Boolean, updater: String) {
        // TODO: Implement with jOOQ when needed
    }
}