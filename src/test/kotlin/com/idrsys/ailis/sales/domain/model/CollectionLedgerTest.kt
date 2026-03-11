package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CollectionLedgerTest {

    @Test
    fun `should create ledger for demand with valid data`() {
        // Given
        val custCd = "CUST001"
        val demandDt = LocalDate.of(2025, 12, 31)
        val demandCharge = BigDecimal("9900000")
        val creator = "admin"

        // When
        val ledger = CollectionLedger.createForDemand(
            custCd = custCd,
            demandDt = demandDt,
            demandCharge = demandCharge,
            creator = creator
        )

        // Then
        assertNotNull(ledger.colledgerId)
        assertEquals(CollectionLedger.DIV_DEMAND, ledger.colbillDivCd)
        assertEquals(demandDt, ledger.colbillDt)
        assertEquals(custCd, ledger.custCd)
        assertEquals("청구", ledger.colbillItemNm)
        assertEquals(demandCharge, ledger.colbillAmt)
        assertEquals(creator, ledger.creator)
        assertTrue(ledger.isNew())
    }

    @Test
    fun `should create ledger for collection with card payment`() {
        // Given
        val custCd = "CUST001"
        val colbillDt = LocalDate.of(2026, 1, 5)
        val payAmt = BigDecimal("5000000")
        val creator = "admin"

        // When
        val ledger = CollectionLedger.createForCollection(
            custCd = custCd,
            colbillDt = colbillDt,
            colbillItemNm = "결제(카드)",
            colbillItemDtl = null,
            payAmt = payAmt,
            creator = creator
        )

        // Then
        assertNotNull(ledger.colledgerId)
        assertEquals(CollectionLedger.DIV_COLLECTION, ledger.colbillDivCd)
        assertEquals(colbillDt, ledger.colbillDt)
        assertEquals(custCd, ledger.custCd)
        assertEquals("결제(카드)", ledger.colbillItemNm)
        assertNull(ledger.colbillItemDtl)
        assertEquals(payAmt, ledger.colbillAmt)
        assertEquals(creator, ledger.creator)
        assertTrue(ledger.isNew())
    }

    @Test
    fun `should create ledger for collection with bank deposit`() {
        // Given
        val custCd = "CUST001"
        val colbillDt = LocalDate.of(2026, 1, 10)
        val payAmt = BigDecimal("3000000")
        val creator = "admin"

        // When
        val ledger = CollectionLedger.createForCollection(
            custCd = custCd,
            colbillDt = colbillDt,
            colbillItemNm = "결제(은행)",
            colbillItemDtl = "외환은행:xxxxxxx",
            payAmt = payAmt,
            creator = creator
        )

        // Then
        assertEquals("결제(은행)", ledger.colbillItemNm)
        assertEquals(payAmt, ledger.colbillAmt)
    }

    @Test
    fun `should throw exception for empty customer code`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            CollectionLedger.createForDemand(
                custCd = "",
                demandDt = LocalDate.now(),
                demandCharge = BigDecimal.ZERO,
                creator = "admin"
            )
        }
    }

    @Test
    fun `should throw exception for negative payment amount`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            CollectionLedger.createForCollection(
                custCd = "CUST001",
                colbillDt = LocalDate.now(),
                colbillItemNm = "결제(카드)",
                colbillItemDtl = null,
                payAmt = BigDecimal("-1000"),
                creator = "admin"
            )
        }
    }
}
