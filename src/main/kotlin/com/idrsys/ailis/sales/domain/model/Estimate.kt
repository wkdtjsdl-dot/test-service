package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Estimate (견적서/거래명세서) Aggregate Root
 *
 * Responsibilities:
 * - Manage estimate and transaction statement information
 * - Calculate item-level amounts and totals
 *
 * Business Rules:
 * - Document number format: GCG-{YYYY}-{TYPE}-{SEQNO}
 * - Document type: "EST" (견적서) or "TRN" (거래명세서)
 * - Totals are sum of all item amounts
 * - Recalculation triggered when items are added/removed/modified
 */
@Table("sales_scm.sbl_estimate")
class Estimate(
    estimateId: String?,
    docType: String,
    docNo: String,
    regDt: LocalDate,
    title: String,
    receiver: String? = null,
    reference: String? = null,
    writerEmpNo: String? = null,
    deptCd: String? = null,
    remark: String? = null,
    note: String? = null,
    totalSupval: BigDecimal = BigDecimal.ZERO,
    totalAddtax: BigDecimal = BigDecimal.ZERO,
    totalAmt: BigDecimal = BigDecimal.ZERO,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String = creator,
    updateDtime: LocalDateTime = LocalDateTime.now()
) : Persistable<String> {

    init {
        require(title.isNotBlank()) { "Title is required" }
        require(docType == DOC_TYPE_ESTIMATE || docType == DOC_TYPE_TRANSACTION) {
            "Document type must be EST or TRN"
        }
    }

    @Id
    @Column("estimate_id")
    val estimateId: String? = estimateId

    @Column("doc_type")
    var docType: String = docType
        private set

    @Column("doc_no")
    var docNo: String = docNo
        private set

    @Column("reg_dt")
    var regDt: LocalDate = regDt
        private set

    @Column("title")
    var title: String = title
        private set

    @Column("receiver")
    var receiver: String? = receiver
        private set

    @Column("reference")
    var reference: String? = reference
        private set

    @Column("writer_emp_no")
    var writerEmpNo: String? = writerEmpNo
        private set

    @Column("dept_cd")
    var deptCd: String? = deptCd
        private set

    @Column("remark")
    var remark: String? = remark
        private set

    @Column("note")
    var note: String? = note
        private set

    @Column("total_supval")
    var totalSupval: BigDecimal = totalSupval
        private set

    @Column("total_addtax")
    var totalAddtax: BigDecimal = totalAddtax
        private set

    @Column("total_amt")
    var totalAmt: BigDecimal = totalAmt
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
    private var _isNew: Boolean = true

    fun setAsNew() {
        this._isNew = true
    }

    override fun getId(): String? = estimateId
    override fun isNew(): Boolean = _isNew

    /**
     * Recalculate totals from items
     *
     * Business Rule:
     * - Total supply value = Σ(item supply values)
     * - Total VAT = Σ(item VATs)
     * - Total amount = Σ(item total amounts)
     */
    fun recalculateTotals(items: List<EstimateItem>) {
        this.totalSupval = items.sumOf { it.supval }
        this.totalAddtax = items.sumOf { it.addtax }
        this.totalAmt = items.sumOf { it.totalAmt }
        this.updateDtime = LocalDateTime.now()
    }

    companion object {
        const val DOC_TYPE_ESTIMATE = "EST"  // 견적서
        const val DOC_TYPE_TRANSACTION = "TRN"  // 거래명세서
    }
}
