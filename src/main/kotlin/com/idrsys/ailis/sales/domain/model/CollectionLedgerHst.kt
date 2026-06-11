package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.sbl_colledger_hst")
class CollectionLedgerHst(
    colledgerHstId: String? = null,
    hstCd: String,
    hstMemo: String,
    worker: String,
    workDtime: LocalDateTime,
    colledgerId: String,
    colbillDivCd: String,
    colbillDt: LocalDate,
    custCd: String,
    colbillItemNm: String?,
    colbillItemDtl: String?,
    colbillAmt: BigDecimal,
    colbillMemo: String?,
    updateReason: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) {
    @Id
    @UuidGeneratedId(idFieldName = "colledgerHstId")
    @Column("colledger_hst_id")
    val colledgerHstId: String? = colledgerHstId

    @Column("hst_cd")
    val hstCd: String = hstCd

    @Column("hst_memo")
    val hstMemo: String = hstMemo

    @Column("worker")
    val worker: String = worker

    @Column("work_dtime")
    val workDtime: LocalDateTime = workDtime

    @Column("colledger_id")
    val colledgerId: String = colledgerId

    @Column("colbill_div_cd")
    val colbillDivCd: String = colbillDivCd

    @Column("colbill_dt")
    val colbillDt: LocalDate = colbillDt

    @Column("cust_cd")
    val custCd: String = custCd

    @Column("colbill_item_nm")
    val colbillItemNm: String? = colbillItemNm

    @Column("colbill_item_dtl")
    val colbillItemDtl: String? = colbillItemDtl

    @Column("colbill_amt")
    val colbillAmt: BigDecimal = colbillAmt

    @Column("colbill_memo")
    val colbillMemo: String? = colbillMemo

    @Column("update_reason")
    val updateReason: String? = updateReason

    @Column("creator")
    val creator: String = creator

    @Column("create_dtime")
    val createDtime: LocalDateTime = createDtime

    @Column("updater")
    val updater: String = updater

    @Column("update_dtime")
    val updateDtime: LocalDateTime = updateDtime

    companion object {
        fun of(
            ledger: CollectionLedger,
            hstCd: String,
            hstMemo: String,
            worker: String,
            workDtime: LocalDateTime,
        ): CollectionLedgerHst = CollectionLedgerHst(
            hstCd = hstCd,
            hstMemo = hstMemo,
            worker = worker,
            workDtime = workDtime,
            colledgerId = ledger.colledgerId!!,
            colbillDivCd = ledger.colbillDivCd,
            colbillDt = ledger.colbillDt,
            custCd = ledger.custCd,
            colbillItemNm = ledger.colbillItemNm,
            colbillItemDtl = ledger.colbillItemDtl,
            colbillAmt = ledger.colbillAmt,
            colbillMemo = ledger.colbillMemo,
            updateReason = null,
            creator = ledger.creator,
            createDtime = ledger.createDtime,
            updater = ledger.updater,
            updateDtime = ledger.updateDtime,
        )
    }
}
