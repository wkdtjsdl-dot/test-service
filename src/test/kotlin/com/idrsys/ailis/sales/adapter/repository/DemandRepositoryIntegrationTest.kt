package com.idrsys.ailis.sales.adapter.repository

import com.idrsys.ailis.sales.adapter.repository.billing.DemandDataRepository
import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.reactive.TransactionalOperator
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * DemandRepository Integration Test
 *
 * Testing reactive R2DBC repository operations
 */
@SpringBootTest
@TestPropertySource(
    properties = [
        "spring.cloud.config.enabled=false",
        "spring.config.import="
    ]
)
class DemandRepositoryIntegrationTest {

    @Autowired
    private lateinit var demandDataRepository: DemandDataRepository

    @Autowired
    private lateinit var r2dbcEntityTemplate: R2dbcEntityTemplate

    @Autowired
    private lateinit var transactionalOperator: TransactionalOperator

    @BeforeEach
    fun setUp(): Unit = runBlocking {
        // Clean up test data
        r2dbcEntityTemplate
            .delete(Demand::class.java)
            .all()
            .`as`(transactionalOperator::transactional)
            .block()
    }

    @Test
    fun `should save and find demand by id`() = runBlocking {
        // Arrange
        val demand = createTestDemand()
        demand.setAsNew()

        // Act
        val saved = demandDataRepository.save(demand)
        val found = demandDataRepository.findById(saved.demandId!!)

        // Assert
        assertNotNull(found)
        assertEquals("CUST001", found.custCd)
        assertEquals(BigDecimal("9900000"), found.demandCharge)
    }

    @Test
    fun `should find demands by customer code`() = runBlocking {
        // Arrange
        val demand1 = createTestDemand(custCd = "CUST001")
        val demand2 = createTestDemand(custCd = "CUST001")
        val demand3 = createTestDemand(custCd = "CUST002")

        demand1.setAsNew()
        demand2.setAsNew()
        demand3.setAsNew()

        demandDataRepository.save(demand1)
        demandDataRepository.save(demand2)
        demandDataRepository.save(demand3)

        // Act
        val found = demandDataRepository.findByCustCd("CUST001").toList()

        // Assert
        assertEquals(2, found.size)
        found.forEach { assertEquals("CUST001", it.custCd) }
    }

    @Test
    fun `should find demands by period`() = runBlocking {
        // Arrange
        val demand1 = createTestDemand(
            demandStartDt = LocalDate.of(2025, 12, 1),
            demandStndDt = LocalDate.of(2025, 12, 31)
        )
        val demand2 = createTestDemand(
            demandStartDt = LocalDate.of(2026, 1, 1),
            demandStndDt = LocalDate.of(2026, 1, 31)
        )

        demand1.setAsNew()
        demand2.setAsNew()

        demandDataRepository.save(demand1)
        demandDataRepository.save(demand2)

        // Act
        val found = demandDataRepository.findByDemandStndDtBetween(
            startDate = LocalDate.of(2025, 12, 1),
            endDate = LocalDate.of(2025, 12, 31)
        ).toList()

        // Assert
        assertEquals(1, found.size)
        assertEquals(LocalDate.of(2025, 12, 31), found[0].demandStndDt)
    }

    @Test
    fun `should delete demand by id`() = runBlocking {
        // Arrange
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = demandDataRepository.save(demand)

        // Act
        demandDataRepository.deleteById(saved.demandId!!)
        val found = demandDataRepository.findById(saved.demandId!!)

        // Assert
        assertNull(found)
    }

    @Test
    fun `should update demand charges`() = runBlocking {
        // Arrange
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = demandDataRepository.save(demand)

        // Act
        saved.recalculateCharges(
            newSupval = BigDecimal("8500000"),
            newAddtax = BigDecimal("850000"),
            updater = "admin"
        )
        val updated = demandDataRepository.save(saved)
        val found = demandDataRepository.findById(updated.demandId!!)

        // Assert
        assertNotNull(found)
        assertEquals(BigDecimal("8500000"), found.supval)
        assertEquals(BigDecimal("850000"), found.addtax)
        assertEquals(BigDecimal("9350000"), found.demandCharge)
    }

    @Test
    fun `should send sales statement`() = runBlocking {
        // Arrange
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = demandDataRepository.save(demand)

        // Act
        saved.sendSalesStatement("SL-2025-001", "EMP001")
        val updated = demandDataRepository.save(saved)
        val found = demandDataRepository.findById(updated.demandId!!)

        // Assert
        assertNotNull(found)
        assertEquals("SL-2025-001", found.slstmtNo)
        assertNotNull(found.slstmtSendDt)
        assertEquals("EMP001", found.slstmtSendEmpNo)
    }

    private fun createTestDemand(
        custCd: String = "CUST001",
        demandStartDt: LocalDate = LocalDate.of(2025, 12, 1),
        demandStndDt: LocalDate = LocalDate.of(2025, 12, 31)
    ): Demand {
        return Demand(
            demandId = UUID.randomUUID().toString(),
            demandDt = demandStndDt,
            custCd = custCd,
            demandStartDt = demandStartDt,
            demandStndDt = demandStndDt,
            stndPrice = BigDecimal("10000000"),
            supval = BigDecimal("9000000"),
            demandCharge = BigDecimal("9900000"),
            addtax = BigDecimal("900000"),
            dscntRate = BigDecimal("1.00"),
            creator = "admin"
        )
    }
}
