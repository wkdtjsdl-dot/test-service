package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * CollectionLedger (청구수금원장) Aggregate Root
 *
 * Responsibilities:
 * - Record billing and collection transaction history in ledger format
 * - Track accounts receivable balance per customer
 * - Provide transaction history query
 *
 * Business Rules:
 * - Division code "0" represents billing/demand
 * - Division code "1" represents collection/payment
 * - Item name for demand is always "청구"
 * - AR balance = Total demand - Total collection
 */
@Table("sales_scm.sbl_colledger")
class CollectionLedger(
    colledgerId: String?,
    colbillDivCd: String,
    colbillDt: LocalDate,
    custCd: String,
    colbillItemNm: String,
    colbillItemDtl: String? = null,
    colbillAmt: BigDecimal,
    colbillMemo: String? = null,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String = creator,
    updateDtime: LocalDateTime = LocalDateTime.now()
) : Persistable<String> {

    init {
        require(custCd.isNotBlank()) { "Customer code is required" }
        require(colbillItemNm.isNotBlank()) { "Item name is required" }
        require(colbillAmt >= BigDecimal.ZERO) { "Payment amount must be non-negative" }
    }

    @Id
    @UuidGeneratedId(idFieldName = "colledgerId")
    @Column("colledger_id")
    val colledgerId: String? = colledgerId

    @Column("colbill_div_cd")
    var colbillDivCd: String = colbillDivCd
        private set

    @Column("colbill_dt")
    var colbillDt: LocalDate = colbillDt
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("colbill_item_nm")
    var colbillItemNm: String = colbillItemNm
        private set

    @Column("colbill_item_dtl")
    var colbillItemDtl: String? = colbillItemDtl
        private set

    @Column("colbill_amt")
    var colbillAmt: BigDecimal = colbillAmt
        private set

    @Column("colbill_memo")
    var colbillMemo: String? = colbillMemo
        private set

    @Column("creator")
    var creator: String = creator
        private set

    @Column("create_dtime")
    var createDtime: LocalDateTime = createDtime
        private set

    @Column("updater")
    var updater: String = updater
        private set

    @Column("update_dtime")
    var updateDtime: LocalDateTime = updateDtime
        private set

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = colledgerId
    override fun isNew(): Boolean = _isNew

    companion object {
        const val DIV_DEMAND = "0"  // 청구
        const val DIV_COLLECTION = "1"  // 수금

        /**
         * Create ledger for demand
         */
        fun createForDemand(
            custCd: String,
            demandDt: LocalDate,
            demandCharge: BigDecimal,
            creator: String
        ): CollectionLedger {
            return CollectionLedger(
                colledgerId = UUID.randomUUID().toString(),
                colbillDivCd = DIV_DEMAND,
                colbillDt = demandDt,
                custCd = custCd,
                colbillItemNm = "청구",
                colbillAmt = demandCharge,
                creator = creator,
                createDtime = LocalDateTime.now(),
                updater = creator,
                updateDtime = LocalDateTime.now()
            ).apply { setAsNew() }
        }

        /**
         * Create ledger for collection
         */
        fun createForCollection(
            custCd: String,
            colbillDt: LocalDate,
            colbillItemNm: String,
            payAmt: BigDecimal,
            creator: String
        ): CollectionLedger {
            return CollectionLedger(
                colledgerId = UUID.randomUUID().toString(),
                colbillDivCd = DIV_COLLECTION,
                colbillDt = colbillDt,
                custCd = custCd,
                colbillItemNm = colbillItemNm,
                colbillAmt = payAmt,
                creator = creator,
                createDtime = LocalDateTime.now(),
                updater = creator,
                updateDtime = LocalDateTime.now()
            ).apply { setAsNew() }
        }
    }
}
