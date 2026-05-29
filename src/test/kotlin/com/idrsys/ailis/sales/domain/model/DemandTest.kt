package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DemandTest {

    @Test
    fun `should create demand with valid data`() {
        // Given
        val custCd = "CUST001"
        val demandDt = LocalDate.of(2025, 12, 31)
        val demandStartDt = LocalDate.of(2025, 12, 1)
        val demandEndDt = LocalDate.of(2025, 12, 31)
        val supval = BigDecimal("9000000")
        val addtax = BigDecimal("900000")
        val demandCharge = BigDecimal("9900000")
        val creator = "admin"

        // When
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = demandDt,
            custCd = custCd,
            demandStartDt = demandStartDt,
            demandEndDt = demandEndDt,
            stndPrice = BigDecimal("10000000"),
            supval = supval,
            demandCharge = demandCharge,
            addtax = addtax,
            dscntRate = BigDecimal("1.0"),
            creator = creator
        )

        // Then
        assertEquals("demand-uuid-001", demand.demandId)
        assertEquals(demandDt, demand.demandDt)
        assertEquals(custCd, demand.custCd)
        assertEquals(demandCharge, demand.demandCharge)
        assertNull(demand.slstmtNo)
        assertEquals(creator, demand.creator)
    }

    @Test
    fun `should throw exception when customer code is empty`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            Demand(
                demandId = "demand-uuid-001",
                demandDt = LocalDate.now(),
                custCd = "",
                demandStartDt = LocalDate.now(),
                demandEndDt = LocalDate.now(),
                stndPrice = BigDecimal.ZERO,
                supval = BigDecimal.ZERO,
                demandCharge = BigDecimal.ZERO,
                addtax = BigDecimal.ZERO,
                dscntRate = BigDecimal.ZERO,
                creator = "admin"
            )
        }
    }

    @Test
    fun `should throw exception when start date is after end date`() {
        // Given & When & Then
        assertThrows<IllegalArgumentException> {
            Demand(
                demandId = "demand-uuid-001",
                demandDt = LocalDate.now(),
                custCd = "CUST001",
                demandStartDt = LocalDate.of(2025, 12, 31),
                demandEndDt = LocalDate.of(2025, 12, 1),
                stndPrice = BigDecimal.ZERO,
                supval = BigDecimal.ZERO,
                demandCharge = BigDecimal.ZERO,
                addtax = BigDecimal.ZERO,
                dscntRate = BigDecimal.ZERO,
                creator = "admin"
            )
        }
    }

    @Test
    fun `should recalculate demand charges correctly`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal("10000000"),
            supval = BigDecimal("9000000"),
            demandCharge = BigDecimal("9900000"),
            addtax = BigDecimal("900000"),
            dscntRate = BigDecimal("1.0"),
            creator = "admin"
        )

        // When
        demand.recalculateCharges(
            newSupval = BigDecimal("8500000"),
            newAddtax = BigDecimal("850000"),
            updater = "admin"
        )

        // Then
        assertEquals(BigDecimal("8500000"), demand.supval)
        assertEquals(BigDecimal("850000"), demand.addtax)
        assertEquals(BigDecimal("9350000"), demand.demandCharge)
        assertEquals("admin", demand.updater)
    }

    @Test
    fun `should send sales statement successfully`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal.ZERO,
            supval = BigDecimal.ZERO,
            demandCharge = BigDecimal.ZERO,
            addtax = BigDecimal.ZERO,
            dscntRate = BigDecimal.ZERO,
            creator = "admin"
        )

        // When
        demand.sendSalesStatement("SL-2025-001", "admin")

        // Then
        assertEquals("SL-2025-001", demand.slstmtNo)
        assertEquals(LocalDate.now(), demand.slstmtSendDt)
        assertEquals("admin", demand.updater)
    }

    @Test
    fun `should keep bill publication flag when sending sales statement`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal.ZERO,
            supval = BigDecimal.ZERO,
            demandCharge = BigDecimal.ZERO,
            addtax = BigDecimal.ZERO,
            dscntRate = BigDecimal.ZERO,
            billPublYn = false,
            creator = "admin"
        )

        // When
        demand.sendSalesStatement("SL-2025-001", "admin")

        // Then
        assertEquals("SL-2025-001", demand.slstmtNo)
        assertFalse(demand.billPublYn)
    }

    @Test
    fun `should throw exception when sales statement already sent`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal.ZERO,
            supval = BigDecimal.ZERO,
            demandCharge = BigDecimal.ZERO,
            addtax = BigDecimal.ZERO,
            dscntRate = BigDecimal.ZERO,
            creator = "admin"
        )
        demand.sendSalesStatement("SL-2025-001", "admin")

        // When & Then
        assertThrows<IllegalStateException> {
            demand.sendSalesStatement("SL-2025-002", "admin")
        }
    }

    @Test
    fun `canCancel should return true when statement not sent`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal.ZERO,
            supval = BigDecimal.ZERO,
            demandCharge = BigDecimal.ZERO,
            addtax = BigDecimal.ZERO,
            dscntRate = BigDecimal.ZERO,
            creator = "admin"
        )

        // When & Then
        assertTrue(demand.canCancel())
    }

    @Test
    fun `canCancel should return false when statement already sent`() {
        // Given
        val demand = Demand(
            demandId = "demand-uuid-001",
            demandDt = LocalDate.now(),
            custCd = "CUST001",
            demandStartDt = LocalDate.now(),
            demandEndDt = LocalDate.now(),
            stndPrice = BigDecimal.ZERO,
            supval = BigDecimal.ZERO,
            demandCharge = BigDecimal.ZERO,
            addtax = BigDecimal.ZERO,
            dscntRate = BigDecimal.ZERO,
            creator = "admin"
        )
        demand.sendSalesStatement("SL-2025-001", "admin")

        // When & Then
        assertFalse(demand.canCancel())
    }
}
