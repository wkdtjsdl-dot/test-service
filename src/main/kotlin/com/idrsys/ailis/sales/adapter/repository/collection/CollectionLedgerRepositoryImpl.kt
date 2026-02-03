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

            // 서브쿼리: 날짜별 그룹화
        val dailySummary = dslContext
            .select(
                cl.CUST_CD.`as`("cust_cd"),
                cl.COLBILL_DT.`as`("colbill_dt"),
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
                ).`as`("rest")
            )
            .from(cl)
            .where(cl.CUST_CD.eq(custCd))
            .groupBy(cl.CUST_CD, cl.COLBILL_DT)
            .asTable("daily_summary")

        // 메인 쿼리: 기존 select 필드 유지 + 누적 잔액 계산
        val query = dslContext
            .select(
                // 기존 필드들 유지
                cl.COLLEDGER_ID,
                cl.COLBILL_DT,
                cl.COLBILL_DIV_CD,
                cl.COLBILL_ITEM_NM,
                cl.COLBILL_AMT,
                // 날짜별 집계된 demand
                dailySummary.field("demand", BigDecimal::class.java)?.`as`("demand_amt"),
                // 날짜별 집계된 collect
                dailySummary.field("collect", BigDecimal::class.java)?.`as`("collect_amt"),
                // 누적 잔액 (SQL의 sum(rest) over 로직)
                DSL.sum(dailySummary.field("rest", BigDecimal::class.java))
                    .over()
                    .orderBy(dailySummary.field("colbill_dt"))
                    .`as`("balance")
            )
            .from(cl)
            .join(dailySummary)
            .on(cl.COLBILL_DT.eq(dailySummary.field("colbill_dt", LocalDate::class.java)))
            .where(cl.CUST_CD.eq(custCd))
            .orderBy(cl.COLBILL_DT, cl.COLLEDGER_ID)

        var executeSpec = databaseClient.sql { query.sql }
        query.bindValues.forEachIndexed { index, value ->
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
        val divCd = row["colbill_div_cd"] as String
        return CollectionLedgerTransaction(
            colledgerId = row["colledger_id"] as String,
            colbillDt = row["colbill_dt"] as LocalDate,
            division = if (divCd == "0") "청구" else "수금",
            colbillItemNm = row["colbill_item_nm"] as? String,
            colbillAmt = row["colbill_amt"] as BigDecimal,
            demandAmt = row["demand_amt"] as BigDecimal,
            collectAmt = row["collect_amt"] as BigDecimal,
            balance = row["balance"] as BigDecimal
        )
    }
}
