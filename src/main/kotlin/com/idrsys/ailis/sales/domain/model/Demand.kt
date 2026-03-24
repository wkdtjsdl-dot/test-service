package com.idrsys.ailis.sales.domain.model

import com.idrsys.common.kor2dbc.generator.UuidGeneratedId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Demand (청구마스터) Aggregate Root
 *
 * Responsibilities:
 * - Manage billing settlement information
 * - Calculate and finalize billing amounts
 * - Manage ERP sales statement integration
 *
 * Business Rules:
 * - Customer code must not be empty
 * - Demand charge = Supply value + VAT
 * - Demand period must be valid (start <= end)
 * - Sales statement can only be sent once
 */
@Table("sales_scm.sbl_demand")
class Demand(
    demandId: String?,
    demandDt: LocalDate,
    custCd: String,
    demandStartDt: LocalDate,
    demandEndDt: LocalDate,
    stndPrice: BigDecimal,
    supval: BigDecimal,
    demandCharge: BigDecimal,
    addtax: BigDecimal,
    dscntRate: BigDecimal,
    demandCreateDtime: LocalDateTime = LocalDateTime.now(),
    demandCreatorEmpNo: String? = null,
    insurePrice: BigDecimal? = null,
    invcOutputDtime: LocalDateTime? = null,
    invcOutputEmpno: String? = null,
    demandMemo: String? = null,
    sapCustCd: String? = null,
    billPublYn: Boolean = false,
    invcRecpEmailAddr: String? = null,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String = creator,
    updateDtime: LocalDateTime = LocalDateTime.now(),
    colledgerId: String? = null,
    crcyCd: String? = null,
) : Persistable<String> {

    init {
        require(custCd.isNotBlank()) { "Customer code cannot be empty" }
//        require(demandCharge >= BigDecimal.ZERO) { "Demand charge must be non-negative" } -> 수기로 수정하는 데이터의 경우, (-) 금액이 있을 수 있음.
        require(demandStartDt <= demandEndDt) { "Start date must be before or equal to end date" }
        require(creator.isNotBlank()) { "Creator is required" }
    }

    @Id
    @UuidGeneratedId(idFieldName = "demandId")
    @Column("demand_id")
    val demandId: String? = demandId

    @Column("demand_dt")
    var demandDt: LocalDate = demandDt
        private set

    @Column("cust_cd")
    var custCd: String = custCd
        private set

    @Column("demand_start_dt")
    var demandStartDt: LocalDate = demandStartDt
        private set

    @Column("demand_end_dt")
    var demandEndDt: LocalDate = demandEndDt
        private set

    @Column("stnd_price")
    var stndPrice: BigDecimal = stndPrice
        private set

    @Column("supval")
    var supval: BigDecimal = supval
        private set

    @Column("demand_charge")
    var demandCharge: BigDecimal = demandCharge
        private set

    @Column("addtax")
    var addtax: BigDecimal = addtax
        private set

    @Column("dscnt_rate")
    var dscntRate: BigDecimal = dscntRate
        private set

    @Column("demand_create_dtime")
    var demandCreateDtime: LocalDateTime = demandCreateDtime
        private set

    @Column("demand_creator_emp_no")
    var demandCreatorEmpNo: String? = demandCreatorEmpNo
        private set

    @Column("insure_price")
    var insurePrice: BigDecimal? = insurePrice
        private set

    @Column("invc_output_dtime")
    var invcOutputDtime: LocalDateTime? = invcOutputDtime
        private set

    @Column("invc_output_empno")
    var invcOutputEmpno: String? = invcOutputEmpno
        private set

    @Column("slstmt_no")
    var slstmtNo: String? = null
        private set

    @Column("slstmt_send_dt")
    var slstmtSendDt: LocalDate? = null
        private set

    @Column("slstmt_send_emp_no")
    var slstmtSendEmpNo: String? = null
        private set

    @Column("demand_memo")
    var demandMemo: String? = demandMemo
        private set

    @Column("sap_cust_cd")
    var sapCustCd: String? = sapCustCd
        private set

    @Column("bill_publ_yn")
    var billPublYn: Boolean = billPublYn
        private set

    @Column("invc_recp_email_addr")
    var invcRecpEmailAddr: String? = invcRecpEmailAddr
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

    @Column("colledger_id")
    var colledgerId: String? = colledgerId
        private set

    @Column("crcy_cd")
    var crcyCd: String? = crcyCd

    @Transient
    private var _isNew: Boolean = false

    fun setAsNew() {
        this._isNew = true
    }

    fun setAsExisting() {
        this._isNew = false
    }

    override fun getId(): String? = demandId
    override fun isNew(): Boolean = _isNew

    /**
     * Recalculate demand charges
     *
     * Business Rule:
     * - Demand charge = Supply value + VAT
     * - Discount rate = (Standard price - Demand charge) / Standard price * 100
     */
    fun recalculateCharges(
        newSupval: BigDecimal,
        newAddtax: BigDecimal,
        updater: String
    ) {
//        require(newAddtax >= BigDecimal.ZERO) { "VAT cannot be negative" } -> 수기로 조정하는 금액의 경우 (-) 세금이 있을 수 있음

        this.supval = newSupval
        this.addtax = newAddtax
        this.demandCharge = newSupval + newAddtax
        this.dscntRate = if (stndPrice > BigDecimal.ZERO) {
            ((stndPrice - demandCharge).divide(stndPrice, 4, RoundingMode.HALF_UP))
                .multiply(BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }
        this.updater = updater
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * Send sales statement to ERP
     *
     * Business Rule:
     * - Can only be sent once
     * - After sending, demand cannot be cancelled
     */
    fun sendSalesStatement(
        slstmtNo: String,
        sendEmpNo: String
    ) {
        require(slstmtNo.isNotBlank()) { "Statement number is required" }
        check(this.slstmtNo == null) { "Sales statement already sent" }

        this.slstmtNo = slstmtNo
        this.slstmtSendDt = LocalDate.now()
        this.slstmtSendEmpNo = sendEmpNo
        this.updater = sendEmpNo
        this.updateDtime = LocalDateTime.now()
    }

    /**
     * Check if demand can be cancelled
     *
     * Business Rule:
     * - Can only be cancelled before ERP statement is sent
     */
    fun canCancel(): Boolean {
        return slstmtNo == null
    }

    /**
     * Assign collection ledger ID
     *
     * Business Rule:
     * - Links demand to its collection ledger entry
     */
    fun assignColledgerId(colledgerId: String) {
        require(colledgerId.isNotBlank()) { "Collection ledger ID cannot be blank" }
        this.colledgerId = colledgerId
    }
}
