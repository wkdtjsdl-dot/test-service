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
        val seq = 1
        val item = "BRCA1/2 유전자 검사"
        val qnty = BigDecimal("100")
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
        assertNotNull(estimateItem.estimateDtlId)
        assertEquals(estimateId, estimateItem.estimateId)
        assertEquals(seq, estimateItem.seq)
        assertEquals(item, estimateItem.item)
        assertEquals(qnty, estimateItem.qnty)
        assertEquals(unitPrice, estimateItem.unitPrice)
        assertEquals(BigDecimal("9000000"), estimateItem.supval)
        assertEquals(BigDecimal("900000"), estimateItem.addtax)
        assertEquals(BigDecimal("9900000"), estimateItem.demandCharge)
        assertEquals(creator, estimateItem.creator)
    }

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
