package com.idrsys.ailis.sales.domain.model

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SalesTargetTest {

    @Test
    fun `should create sales target with valid data`() {
        // Given
        val now = LocalDateTime.now()

        // When
        val salesTarget = SalesTarget(
            salesTargetId = "target-uuid-001",
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            monthSalesTargetAmt = BigDecimal("10000000"),
            pastYearMonthSalesAmt = BigDecimal("8000000"),
            creator = "admin",
            createDtime = now,
            updater = "admin",
            updateDtime = now
        )

        // Then
        assertEquals("target-uuid-001", salesTarget.salesTargetId)
        assertEquals("CUST001", salesTarget.custCd)
        assertEquals("2026", salesTarget.salesYear)
        assertEquals("01", salesTarget.salesMonth)
        assertEquals("TS-G", salesTarget.salsTeamCd)
        assertEquals(BigDecimal("10000000"), salesTarget.monthSalesTargetAmt)
        assertEquals(BigDecimal("8000000"), salesTarget.pastYearMonthSalesAmt)
        assertEquals("admin", salesTarget.creator)
        assertEquals("admin", salesTarget.updater)
    }

    @Test
    fun `should create sales target with default values`() {
        // Given
        val now = LocalDateTime.now()

        // When
        val salesTarget = SalesTarget(
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            creator = "admin",
            createDtime = now,
            updater = "admin",
            updateDtime = now
        )

        // Then
        assertNull(salesTarget.salesTargetId)
        assertEquals(BigDecimal.ZERO, salesTarget.monthSalesTargetAmt)
        assertEquals(BigDecimal.ZERO, salesTarget.pastYearMonthSalesAmt)
    }

    @Test
    fun `should update sales target amounts`() {
        // Given
        val now = LocalDateTime.now()
        val salesTarget = SalesTarget(
            salesTargetId = "target-uuid-001",
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            monthSalesTargetAmt = BigDecimal("10000000"),
            pastYearMonthSalesAmt = BigDecimal("8000000"),
            creator = "admin",
            createDtime = now,
            updater = "admin",
            updateDtime = now
        )

        // When
        salesTarget.update(
            monthSalesTargetAmt = BigDecimal("12000000"),
            pastYearMonthSalesAmt = BigDecimal("9000000"),
            updater = "manager"
        )

        // Then
        assertEquals(BigDecimal("12000000"), salesTarget.monthSalesTargetAmt)
        assertEquals(BigDecimal("9000000"), salesTarget.pastYearMonthSalesAmt)
        assertEquals("manager", salesTarget.updater)
        assertTrue(salesTarget.updateDtime.isAfter(now) || salesTarget.updateDtime.isEqual(now))
    }

    @Test
    fun `should return correct id from getId method`() {
        // Given
        val salesTarget = SalesTarget(
            salesTargetId = "target-uuid-001",
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        // When & Then
        assertEquals("target-uuid-001", salesTarget.id)
    }

    @Test
    fun `should return false for isNew by default`() {
        // Given
        val salesTarget = SalesTarget(
            salesTargetId = "target-uuid-001",
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        // When & Then
        assertFalse(salesTarget.isNew)
    }

    @Test
    fun `should return true for isNew after setAsNew`() {
        // Given
        val salesTarget = SalesTarget(
            salesTargetId = "target-uuid-001",
            custCd = "CUST001",
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )

        // When
        salesTarget.setAsNew()

        // Then
        assertTrue(salesTarget.isNew)
    }
}
