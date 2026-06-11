package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sales_scm.sbl_colbill_hst")
class CollectionBillHst(
    colbillHstId: String? = null,
    hstCd: String,
    hstMemo: String,
    worker: String,
    workDtime: LocalDateTime,
    colbillId: String,
    custCd: String,
    colbillDt: LocalDate,
    payMethodCd: String,
    cardCompCd: String?,
    cardCompNm: String?,
    payAmt: BigDecimal?,
    cardApprNo: String?,
    cardNo: String?,
    cardPayId: String?,
    cardBillNo: String?,
    instlMonth: String?,
    bankDepositId: String?,
    accountYear: String?,
    surecpSlstmtNo: String?,
    salesSlstmtNo: String?,
    advreceYn: Boolean?,
    closingCd: String?,
    sendYn: Boolean,
    colledgerId: String?,
    remark: String?,
    updateReason: String?,
    creator: String,
    createDtime: LocalDateTime,
    updater: String,
    updateDtime: LocalDateTime,
) {
    @Id
    @UuidGeneratedId(idFieldName = "colbillHstId")
    @Column("colbill_hst_id")
    val colbillHstId: String? = colbillHstId

    @Column("hst_cd")
    val hstCd: String = hstCd

    @Column("hst_memo")
    val hstMemo: String = hstMemo

    @Column("worker")
    val worker: String = worker

    @Column("work_dtime")
    val workDtime: LocalDateTime = workDtime

    @Column("colbill_id")
    val colbillId: String = colbillId

    @Column("cust_cd")
    val custCd: String = custCd

    @Column("colbill_dt")
    val colbillDt: LocalDate = colbillDt

    @Column("pay_method_cd")
    val payMethodCd: String = payMethodCd

    @Column("card_comp_cd")
    val cardCompCd: String? = cardCompCd

    @Column("card_comp_nm")
    val cardCompNm: String? = cardCompNm

    @Column("pay_amt")
    val payAmt: BigDecimal? = payAmt

    @Column("card_appr_no")
    val cardApprNo: String? = cardApprNo

    @Column("card_no")
    val cardNo: String? = cardNo

    @Column("card_pay_id")
    val cardPayId: String? = cardPayId

    @Column("card_bill_no")
    val cardBillNo: String? = cardBillNo

    @Column("instl_month")
    val instlMonth: String? = instlMonth

    @Column("bank_deposit_id")
    val bankDepositId: String? = bankDepositId

    @Column("account_year")
    val accountYear: String? = accountYear

    @Column("surecp_slstmt_no")
    val surecpSlstmtNo: String? = surecpSlstmtNo

    @Column("sales_slstmt_no")
    val salesSlstmtNo: String? = salesSlstmtNo

    @Column("advrece_yn")
    val advreceYn: Boolean? = advreceYn

    @Column("closing_cd")
    val closingCd: String? = closingCd

    @Column("send_yn")
    val sendYn: Boolean = sendYn

    @Column("colledger_id")
    val colledgerId: String? = colledgerId

    @Column("remark")
    val remark: String? = remark

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
            bill: CollectionBill,
            hstCd: String,
            hstMemo: String,
            worker: String,
            workDtime: LocalDateTime,
        ): CollectionBillHst = CollectionBillHst(
            hstCd = hstCd,
            hstMemo = hstMemo,
            worker = worker,
            workDtime = workDtime,
            colbillId = bill.colbillId!!,
            custCd = bill.custCd,
            colbillDt = bill.colbillDt,
            payMethodCd = bill.payMethodCd,
            cardCompCd = bill.cardCompCd,
            cardCompNm = bill.cardCompNm,
            payAmt = bill.payAmt,
            cardApprNo = bill.cardApprNo,
            cardNo = bill.cardNo,
            cardPayId = bill.cardPayId,
            cardBillNo = bill.cardBillNo,
            instlMonth = bill.instlMonth,
            bankDepositId = bill.bankDepositId,
            accountYear = bill.accountYear,
            surecpSlstmtNo = bill.surecpSlstmtNo,
            salesSlstmtNo = bill.salesSlstmtNo,
            advreceYn = bill.advreceYn,
            closingCd = bill.closingCd,
            sendYn = bill.sendYn,
            colledgerId = bill.colledgerId,
            remark = bill.remark,
            updateReason = null,
            creator = bill.creator,
            createDtime = bill.createDtime,
            updater = bill.updater,
            updateDtime = bill.updateDtime,
        )
    }
}
