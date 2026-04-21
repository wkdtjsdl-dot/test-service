package com.idrsys.ailis.sales.adapter.repository.collection

import com.idrsys.ailis.sales.application.dto.response.CollectionLedgerTransaction
import com.idrsys.ailis.sales.application.required.repository.collection.CollectionLedgerRepository
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import com.idrsys.ailis.sales.generated.jooq.SalesScm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class CollectionLedgerRepositoryImpl(
    private val collectionLedgerDataRepository: CollectionLedgerDataRepository,
    private val dslContext: DSLContext,
    private val databaseClient: DatabaseClient
) : CollectionLedgerRepository {

    // Basic CRUD operations (delegated to Spring Data R2DBC)
    override suspend fun save(collectionLedger: CollectionLedger): CollectionLedger {
        return collectionLedgerDataRepository.save(collectionLedger)
    }

    override suspend fun findById(id: String): CollectionLedger? {
        return collectionLedgerDataRepository.findById(id)
    }

    override suspend fun delete(collectionLedger: CollectionLedger) {
        collectionLedgerDataRepository.delete(collectionLedger)
    }

    override fun findByCustCdOrderByColbillDtAsc(
        custCd: String,
    ): Flow<CollectionLedger> {
        return collectionLedgerDataRepository.findByCustCd(custCd)
    }

    override fun findLedgerTransactionsWithBalance(custCd: String): Flow<CollectionLedgerTransaction> {
        val cl = SalesScm.SALES_SCM.SBL_COLLEDGER
        val cb = SalesScm.SALES_SCM.SBL_COLBILL

        // 서브쿼리: 날짜 + 구분코드별 집계
        val summary = dslContext
            .select(
                cl.CUST_CD.`as`("cust_cd"),
                cl.COLBILL_DT.`as`("colbill_dt"),
                cl.COLBILL_DIV_CD.`as`("colbill_div_cd"),
                DSL.max(cl.COLLEDGER_ID).`as`("colledger_id"),
                DSL.sum(cl.COLBILL_AMT).`as`("colbill_amt"), // 금액 합계 추가
                DSL.sum(
                    DSL.case_()
                        .`when`(cl.COLBILL_DIV_CD.eq("0"), cl.COLBILL_AMT)
                        .otherwise(BigDecimal.ZERO)
                ).`as`("demand"),
                DSL.sum(
                    DSL.case_()
                        .`when`(cl.COLBILL_DIV_CD.eq("1"), cl.COLBILL_AMT)
                        .otherwise(BigDecimal.ZERO)
                ).`as`("collect"),
                DSL.sum(
                    DSL.case_()
                        .`when`(cl.COLBILL_DIV_CD.eq("0"), cl.COLBILL_AMT)
                        .otherwise(BigDecimal.ZERO)
                ).minus(
                    DSL.sum(
                        DSL.case_()
                            .`when`(cl.COLBILL_DIV_CD.eq("1"), cl.COLBILL_AMT)
                            .otherwise(BigDecimal.ZERO)
                    )
                ).`as`("rest"),
                DSL.max(cl.COLBILL_ITEM_NM).`as`("colbill_item_nm"),
                DSL.field(
                    DSL.select(cb.ADVRECE_YN)
                        .from(cb)
                        .where(cb.COLLEDGER_ID.eq(DSL.max(cl.COLLEDGER_ID)))
                        .limit(1)
                ).`as`("advrece_yn")
            )
            .from(cl)
            .where(cl.CUST_CD.eq(custCd))
            .groupBy(cl.CUST_CD, cl.COLBILL_DT, cl.COLBILL_DIV_CD)
            .orderBy(
                DSL.field("colbill_dt"),
                DSL.field("colbill_div_cd")
            )

        // 메인 쿼리: 누적 잔액 계산
        val finalQuery = dslContext
            .select(
                DSL.field("colledger_id", String::class.java),
                DSL.field("cust_cd", String::class.java),
                DSL.field("colbill_div_cd", String::class.java),
                DSL.case_()
                    .`when`(DSL.field("colbill_div_cd").eq("0"), DSL.inline("청구"))
                    .otherwise(DSL.inline("수금"))
                    .`as`("colbill_div_nm"),
                DSL.field("colbill_dt", LocalDate::class.java),
                DSL.field("colbill_amt", BigDecimal::class.java), // 금액 추가
                DSL.field("demand", BigDecimal::class.java),
                DSL.field("collect", BigDecimal::class.java),
                DSL.sum(DSL.field("rest", BigDecimal::class.java))
                    .over()
                    .orderBy(
                        DSL.field("colbill_dt"),
                        DSL.field("colbill_div_cd")
                    )
                    .`as`("balance"),
                DSL.field("colbill_item_nm", String::class.java),
                DSL.field("advrece_yn", Boolean::class.java)
            )
            .from(summary.asTable("sub"))
            .orderBy(
                DSL.field("colbill_dt"),
                DSL.field("colbill_div_cd")
            )

        var executeSpec = databaseClient.sql { finalQuery.sql }
        finalQuery.bindValues.forEachIndexed { index, value ->
            if (value != null) {
                executeSpec = executeSpec.bind(index, value)
            } else {
                executeSpec = executeSpec.bindNull(index, String::class.java)
            }
        }

        return executeSpec
            .fetch()
            .all()
            .map { row -> toCollectionLedgerTransaction(row) }
            .asFlow()
    }

    private fun toCollectionLedgerTransaction(row: Map<String, Any>): CollectionLedgerTransaction {
        return CollectionLedgerTransaction(
            colledgerId = row["colledger_id"] as String,
            colbillDt = row["colbill_dt"] as LocalDate,
            division = row["colbill_div_nm"] as String,
            colbillItemNm = row["colbill_item_nm"] as? String,
            colbillAmt = row["colbill_amt"] as BigDecimal, // 금액 매핑
            demandAmt = row["demand"] as BigDecimal,
            collectAmt = row["collect"] as BigDecimal,
            balance = row["balance"] as BigDecimal,
            advreceYn = row["advrece_yn"] as? Boolean ?: false
        )
    }

}
