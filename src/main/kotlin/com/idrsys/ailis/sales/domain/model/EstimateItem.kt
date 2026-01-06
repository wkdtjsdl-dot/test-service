package com.idrsys.ailis.sales.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

/**
 * EstimateItem (견적서 항목) Value Object
 *
 * Business Rules:
 * - Supply value = Quantity × Unit price
 * - VAT = Supply value × 0.1 (rounded half-up)
 * - Total amount = Supply value + VAT
 */
@Table("sales_scm.sbl_estimate_item")
data class EstimateItem(
    @Id
    @Column("estimate_item_id")
    val estimateItemId: String,

    @Column("estimate_id")
    val estimateId: String,

    @Column("item_nm")
    val itemNm: String,

    @Column("qty")
    val qty: BigDecimal,

    @Column("unit_price")
    val unitPrice: BigDecimal,

    @Column("supval")
    val supval: BigDecimal,

    @Column("addtax")
    val addtax: BigDecimal,

    @Column("total_amt")
    val totalAmt: BigDecimal
) {
    companion object {
        /**
         * Create estimate item with auto-calculation
         */
        fun create(
            estimateId: String,
            itemNm: String,
            qty: BigDecimal,
            unitPrice: BigDecimal
        ): EstimateItem {
            require(qty >= BigDecimal.ZERO) { "Quantity must be non-negative" }
            require(unitPrice >= BigDecimal.ZERO) { "Unit price must be non-negative" }

            val supval = qty.multiply(unitPrice).setScale(0, RoundingMode.HALF_UP)
            val addtax = supval.multiply(BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP)
            val totalAmt = supval.add(addtax)

            return EstimateItem(
                estimateItemId = UUID.randomUUID().toString(),
                estimateId = estimateId,
                itemNm = itemNm,
                qty = qty,
                unitPrice = unitPrice,
                supval = supval,
                addtax = addtax,
                totalAmt = totalAmt
            )
        }
    }
}
