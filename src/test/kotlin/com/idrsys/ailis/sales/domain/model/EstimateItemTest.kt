package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EstimateItemTest {

    @Test
    fun `should calculate zero amounts for zero quantity`() {
        // Given
        val estimateId = "estimate-uuid-001"
        val seq = 1
        val item = "Test Item"
        val qnty = BigDecimal.ZERO
        val unitPrice = BigDecimal("90000")
        val creator = "admin-001"

        // When
        val estimateItem = EstimateItem.create(
            estimateId = estimateId,
            seq = seq,
            item = item,
            qnty = qnty,
            unitPrice = unitPrice,
            creator = creator
        )

        // Then
        assertEquals(BigDecimal.ZERO, estimateItem.supval)
        assertEquals(BigDecimal.ZERO, estimateItem.addtax)
        assertEquals(BigDecimal.ZERO, estimateItem.demandCharge)
    }

    @Test
    fun `should throw exception for negative quantity`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                seq = 1,
                item = "Test",
                qnty = BigDecimal("-1"),
                unitPrice = BigDecimal("100"),
                creator = "admin-001"
            )
        }
    }

    @Test
    fun `should throw exception for negative unit price`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                seq = 1,
                item = "Test",
                qnty = BigDecimal("1"),
                unitPrice = BigDecimal("-100"),
                creator = "admin-001"
            )
        }
    }
}
