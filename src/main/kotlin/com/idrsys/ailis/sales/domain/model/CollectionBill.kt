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

/**
 * CollectionBill (수금) Entity
 *
 * Responsibilities:
 * - Manage actual collection data
 * - Link with card payment or bank deposit information
 * - Manage ERP transmission status
 *
 * Business Rules:
 * - Either card payment or bank deposit must be specified, but not both
 * - Initially not sent to ERP (sendYn = false)
 * - Advanced receipt flag defaults to false
 */
@Table("sales_scm.sbl_colbill")
class CollectionBill(
    colbillId: String?,
    custCd: String,
    colbillDt: LocalDate,
    payMethodCd: String,
    payAmt: BigDecimal,
    cardPayId: String? = null,
    bankDepositId: String? = null,
    cardCompCd: String? = null,
    cardCompNm: String? = null,
    cardApprNo: String? = null,
    cardNo: String? = null,
    cardBillNo: String? = null,
    instlMonth: String? = null,
    accountYear: String? = null,
    surecpSlstmtNo: String? = null,
    salesSlstmtNo: String? = null,
    advreceYn: Boolean = false,
    closingCd: String? = null,
    colledgerId: String? = null,
    sendYn: Boolean = false,
    remark: String? = null,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String = creator,
    updateDtime: LocalDateTime = LocalDateTime.now()
) : Persistable<String> {

    init {
        require(custCd.isNotBlank()) { "Customer code is required" }
        require(!(cardPayId != null && bankDepositId != null)) { "Only one payment source allowed" }
    }

    @Id
    @UuidGeneratedId(idFieldName = "colbillId")
    @Column("colbill_id")
    val colbillId: String? = colbillId

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("colbill_dt")
    var colbillDt: LocalDate = colbillDt
        private set

    @Column("pay_method_cd")
    var payMethodCd: String = payMethodCd
        private set

    @Column("pay_amt")
    var payAmt: BigDecimal = payAmt
        private set

    @Column("card_pay_id")
    var cardPayId: String? = cardPayId
        private set

    @Column("bank_deposit_id")
    var bankDepositId: String? = bankDepositId
        private set

    @Column("card_comp_cd")
    var cardCompCd: String? = cardCompCd
        private set

    @Column("card_comp_nm")
    var cardCompNm: String? = cardCompNm
        private set

    @Column("card_appr_no")
    var cardApprNo: String? = cardApprNo
        private set

    @Column("card_no")
    var cardNo: String? = cardNo
        private set

    @Column("card_bill_no")
    var cardBillNo: String? = cardBillNo
        private set

    @Column("instl_month")
    var instlMonth: String? = instlMonth
        private set

    @Column("account_year")
    var accountYear: String? = accountYear
        private set

    @Column("surecp_slstmt_no")
    var surecpSlstmtNo: String? = surecpSlstmtNo
        private set

    @Column("sales_slstmt_no")
    var salesSlstmtNo: String? = salesSlstmtNo
        private set

    @Column("advrece_yn")
    var advreceYn: Boolean = advreceYn
        private set

    @Column("closing_cd")
    var closingCd: String? = closingCd
        private set

    @Column("colledger_id")
    var colledgerId: String? = colledgerId
        private set

    @Column("send_yn")
    var sendYn: Boolean = sendYn
        private set

    @Column("remark")
    var remark: String? = remark

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

    fun setAsExisting() {
        this._isNew = false
    }

    override fun getId(): String? = colbillId
    override fun isNew(): Boolean = _isNew

    /**
     * Mark as sent to ERP
     *
     * Business Rule:
     * - Once sent, the flag cannot be reverted (no rollback in domain)
     */
    fun markAsSent(updater: String, salesSlstmtNo: String? = null) {
        this.sendYn = true
        this.salesSlstmtNo = salesSlstmtNo
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
    fun updateClosingStatus(closingCd: String, updater: String) {
        require(closingCd == "CLCD_Y" || closingCd == "CLCD_N") {
            "Closing code must be CLCD_Y or CLCD_N"
        }
        this.closingCd = closingCd
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }
}
