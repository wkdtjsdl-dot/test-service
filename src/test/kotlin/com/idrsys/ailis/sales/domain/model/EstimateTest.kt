package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EstimateTest {

    @Test
    fun `should create estimate with valid data`() {
        // Given
        val docType = Estimate.DOC_TYPE_ESTIMATE
        val regDt = LocalDate.of(2026, 1, 5)
        val title = "유전자 검사 견적서"
        val creator = "admin"

        // When
        val estimate = Estimate(
            estimateId = "estimate-uuid-001",
            docType = docType,
            docNo = "GCG-2026-EST-000001",
            regDt = regDt,
            title = title,
            creator = creator
        )

        // Then
        assertEquals("estimate-uuid-001", estimate.estimateId)
        assertEquals(docType, estimate.docType)
        assertEquals("GCG-2026-EST-000001", estimate.docNo)
        assertEquals(regDt, estimate.regDt)
        assertEquals(title, estimate.title)
        assertEquals(BigDecimal.ZERO, estimate.totalSupval)
        assertEquals(BigDecimal.ZERO, estimate.totalAddtax)
        assertEquals(BigDecimal.ZERO, estimate.totalAmt)
    }

    @Test
    fun `should throw exception when title is empty`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            Estimate(
                estimateId = "estimate-uuid-001",
                docType = Estimate.DOC_TYPE_ESTIMATE,
                docNo = "GCG-2026-EST-000001",
                regDt = LocalDate.now(),
                title = "",
                creator = "admin"
            )
        }
    }

    @Test
    fun `should throw exception for invalid document type`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            Estimate(
                estimateId = "estimate-uuid-001",
                docType = "INVALID",
                docNo = "GCG-2026-EST-000001",
                regDt = LocalDate.now(),
                title = "Test",
                creator = "admin"
            )
        }
    }

    @Test
    fun `should recalculate totals from items`() {
        // Given
        val estimate = Estimate(
            estimateId = "estimate-uuid-001",
            docType = Estimate.DOC_TYPE_ESTIMATE,
            docNo = "GCG-2026-EST-000001",
            regDt = LocalDate.now(),
            title = "Test",
            creator = "admin"
        )

        val items = listOf(
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                itemNm = "BRCA1/2",
                qty = BigDecimal("100"),
                unitPrice = BigDecimal("90000")
            ),
            EstimateItem.create(
                estimateId = "estimate-uuid-001",
                itemNm = "KRAS",
                qty = BigDecimal("50"),
                unitPrice = BigDecimal("50000")
            )
        )

        // When
        estimate.recalculateTotals(items)

        // Then
        assertEquals(BigDecimal("11500000"), estimate.totalSupval)
        assertEquals(BigDecimal("1150000"), estimate.totalAddtax)
        assertEquals(BigDecimal("12650000"), estimate.totalAmt)
    }
}
