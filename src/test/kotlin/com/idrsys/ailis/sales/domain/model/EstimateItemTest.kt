package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EstimateItemTest {

    @Test
    fun `should create estimate item with auto calculation`() {
        // Given
        val estimateId = "estimate-uuid-001"
        val itemNm = "BRCA1/2 유전자 검사"
        val qty = BigDecimal("100")
        val unitPrice = BigDecimal("90000")

        // When
        val item = EstimateItem.create(
            estimateId = estimateId,
            itemNm = itemNm,
            qty = qty,
            unitPrice = unitPrice
        )

        // Then
        assertNotNull(item.estimateItemId)
        assertEquals(estimateId, item.estimateId)
        assertEquals(itemNm, item.itemNm)
        assertEquals(qty, item.qty)
        assertEquals(unitPrice, item.unitPrice)
        assertEquals(BigDecimal("9000000"), item.supval)
        assertEquals(BigDecimal("900000"), item.addtax)
        assertEquals(BigDecimal("9900000"), item.totalAmt)
    }

    @Test
    fun `should calculate zero amounts for zero quantity`() {
        // Given
        val estimateId = "estimate-uuid-001"
        val itemNm = "Test Item"
        val qty = BigDecimal.ZERO
        val unitPrice = BigDecimal("90000")

        // When
        val item = EstimateItem.create(
            estimateId = estimateId,
            itemNm = itemNm,
            qty = qty,
            unitPrice = unitPrice
        )

        // Then
        assertEquals(BigDecimal.ZERO, item.supval)
        assertEquals(BigDecimal.ZERO, item.addtax)
        assertEquals(BigDecimal.ZERO, item.totalAmt)
    }

    @Test
    fun `should throw exception for negative quantity`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                itemNm = "Test",
                qty = BigDecimal("-1"),
                unitPrice = BigDecimal("100")
            )
        }
    }

    @Test
    fun `should throw exception for negative unit price`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                itemNm = "Test",
                qty = BigDecimal("1"),
                unitPrice = BigDecimal("-100")
            )
        }
    }
}
