package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionBillTest {

    @Test
    fun `should create collection bill for card payment`() {
        // Given
        val custCd = "CUST001"
        val colbillDt = LocalDate.of(2026, 1, 5)
        val payMethodCd = "CARD"
        val payAmt = BigDecimal("5000000")
        val cardPayId = "card-pay-uuid-001"
        val creator = "admin"

        // When
        val bill = CollectionBill(
            colbillId = "colbill-uuid-001",
            custCd = custCd,
            colbillDt = colbillDt,
            payMethodCd = payMethodCd,
            payAmt = payAmt,
            cardPayId = cardPayId,
            creator = creator
        )

        // Then
        assertEquals("colbill-uuid-001", bill.colbillId)
        assertEquals(custCd, bill.custCd)
        assertEquals(colbillDt, bill.colbillDt)
        assertEquals(payMethodCd, bill.payMethodCd)
        assertEquals(payAmt, bill.payAmt)
        assertEquals(cardPayId, bill.cardPayId)
        assertFalse(bill.sendYn)
        assertFalse(bill.advreceYn)
    }

    @Test
    fun `should create collection bill for bank deposit`() {
        // Given
        val custCd = "CUST001"
        val colbillDt = LocalDate.of(2026, 1, 5)
        val payMethodCd = "BANK"
        val payAmt = BigDecimal("10000000")
        val bankDepositId = "bank-deposit-uuid-001"
        val creator = "admin"

        // When
        val bill = CollectionBill(
            colbillId = "colbill-uuid-001",
            custCd = custCd,
            colbillDt = colbillDt,
            payMethodCd = payMethodCd,
            payAmt = payAmt,
            bankDepositId = bankDepositId,
            creator = creator
        )

        // Then
        assertEquals(bankDepositId, bill.bankDepositId)
        assertEquals(payAmt, bill.payAmt)
    }

    @Test
    fun `should throw exception when both payment sources are null`() {
        // Given & When
        val bill = CollectionBill(
            colbillId = "colbill-uuid-001",
            custCd = "CUST001",
            colbillDt = LocalDate.now(),
            payMethodCd = "NONE",
            payAmt = BigDecimal.ZERO,
            cardPayId = null,
            bankDepositId = null,
            creator = "admin"
        )

        // Then
        assertNull(bill.cardPayId)
        assertNull(bill.bankDepositId)
        assertEquals("CUST001", bill.custCd)
    }

    @Test
    fun `should throw exception when both payment sources are provided`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            CollectionBill(
                colbillId = "colbill-uuid-001",
                custCd = "CUST001",
                colbillDt = LocalDate.now(),
                payMethodCd = "CARD",
                payAmt = BigDecimal.ZERO,
                cardPayId = "card-pay-uuid",
                bankDepositId = "bank-deposit-uuid",
                creator = "admin"
            )
        }
    }

    @Test
    fun `should throw exception for empty customer code`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            CollectionBill(
                colbillId = "colbill-uuid-001",
                custCd = "",
                colbillDt = LocalDate.now(),
                payMethodCd = "CARD",
                payAmt = BigDecimal.ZERO,
                cardPayId = "card-pay-uuid",
                creator = "admin"
            )
        }
    }

    @Test
    fun `should mark as sent to ERP`() {
        // Given
        val bill = CollectionBill(
            colbillId = "colbill-uuid-001",
            custCd = "CUST001",
            colbillDt = LocalDate.now(),
            payMethodCd = "CARD",
            payAmt = BigDecimal("5000000"),
            cardPayId = "card-pay-uuid",
            creator = "admin"
        )

        // When
        bill.markAsSent("admin")

        // Then
        assertTrue(bill.sendYn)
        assertEquals("admin", bill.updater)
    }
}
