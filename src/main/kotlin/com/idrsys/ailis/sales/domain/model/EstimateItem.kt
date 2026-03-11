package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.UUID

/**
 * EstimateItem (견적서 항목) Entity
 *
 * Business Rules:
 * - Supply value = Quantity × Unit price
 * - VAT = Supply value × 0.1 (rounded half-up)
 * - Demand charge = Supply value + VAT
 * - Seq is auto-incremented within each estimate
 */
@Table("sales_scm.sbl_estimate_dtl")
class EstimateItem(
    estimateDtlId: String?,
    estimateId: String,
    seq: Int,
    item: String,
    qnty: BigDecimal,
    unitPrice: BigDecimal,
    supval: BigDecimal,
    addtax: BigDecimal,
    demandCharge: BigDecimal,
    creator: String,
    createDtime: LocalDateTime = LocalDateTime.now(),
    updater: String = creator,
    updateDtime: LocalDateTime = LocalDateTime.now()
) : Persistable<String> {

    init {
        require(item.isNotBlank()) { "Item name is required" }
        require(qnty >= BigDecimal.ZERO) { "Quantity must be non-negative" }
        require(unitPrice >= BigDecimal.ZERO) { "Unit price must be non-negative" }
        require(seq >= 0) { "Sequence must be positive" }
    }

    @Id
    @Column("estimate_dtl_id")
    val estimateDtlId: String? = estimateDtlId

    @Column("estimate_id")
    var estimateId: String = estimateId
        private set

    @Column("seq")
    var seq: Int = seq
        private set

    @Column("item")
    var item: String = item
        private set

    @Column("qnty")
    var qnty: BigDecimal = qnty
        private set

    @Column("unit_price")
    var unitPrice: BigDecimal = unitPrice
        private set

    @Column("supval")
    var supval: BigDecimal = supval
        private set

    @Column("addtax")
    var addtax: BigDecimal = addtax
        private set

    @Column("demand_charge")
    var demandCharge: BigDecimal = demandCharge
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

    override fun getId(): String? = estimateDtlId
    override fun isNew(): Boolean = _isNew

    companion object {
        /**
         * Create estimate item with auto-calculation
         *
         * @param estimateId Estimate ID
         * @param seq Sequence number within the estimate
         * @param item Item name
         * @param qnty Quantity
         * @param unitPrice Unit price
         * @param creator Creator ID
         */
        fun create(
            estimateId: String,
            seq: Int,
            item: String,
            qnty: BigDecimal,
            unitPrice: BigDecimal,
            creator: String
        ): EstimateItem {
            val supval = qnty.multiply(unitPrice).setScale(0, RoundingMode.HALF_UP)
            val addtax = supval.multiply(BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP)
            val demandCharge = supval.add(addtax)

            return EstimateItem(
                estimateDtlId = UUID.randomUUID().toString(),
                estimateId = estimateId,
                seq = seq,
                item = item,
                qnty = qnty,
                unitPrice = unitPrice,
                supval = supval,
                addtax = addtax,
                demandCharge = demandCharge,
                creator = creator,
                createDtime = LocalDateTime.now(),
                updater = creator,
                updateDtime = LocalDateTime.now()
            ).apply { setAsNew() }
        }
    }
}
