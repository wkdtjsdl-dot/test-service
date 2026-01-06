package com.idrsys.ailis.sales.adapter.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.idrsys.ailis.sales.application.dto.request.billing.CreateDemandCommand
import com.idrsys.ailis.sales.domain.model.Demand
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.reactive.TransactionalOperator
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

/**
 * Billing Controller Integration Test
 *
 * Testing REST API endpoints with WebTestClient
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(
    properties = [
        "spring.cloud.config.enabled=false",
        "spring.config.import="
    ]
)
class BillingControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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
    fun `should create demand via POST endpoint`() {
        // Arrange
        val command = CreateDemandCommand(
            custCd = "CUST001",
            demandDt = LocalDate.of(2025, 12, 31),
            demandStartDt = LocalDate.of(2025, 12, 1),
            demandStndDt = LocalDate.of(2025, 12, 31),
            exrtId = null,
            demandMemo = "12월 정산"
        )

        // Act & Assert
        webTestClient
            .post()
            .uri("/api/v1/billing/demands")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.demandId").exists()
            .jsonPath("$.custCd").isEqualTo("CUST001")
            .jsonPath("$.colledgerId").exists()
    }

    @Test
    fun `should get demand list via GET endpoint`() = runBlocking {
        // Arrange - Create test demand
        val demand = createTestDemand()
        demand.setAsNew()
        r2dbcEntityTemplate.insert(demand).block()

        // Act & Assert
        webTestClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/api/v1/billing/demands")
                    .queryParam("demandType", "SETTLED")
                    .queryParam("startDt", "2025-12-01")
                    .queryParam("endDt", "2025-12-31")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.totalElements").exists()
    }

    @Test
    fun `should get demand detail via GET endpoint`() = runBlocking {
        // Arrange - Create test demand
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = r2dbcEntityTemplate.insert(demand).block()!!

        // Act & Assert
        webTestClient
            .get()
            .uri("/api/v1/billing/demands/${saved.demandId}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.demandId").isEqualTo(saved.demandId!!)
            .jsonPath("$.custCd").isEqualTo("CUST001")
    }

    @Test
    fun `should cancel demand via DELETE endpoint`() = runBlocking {
        // Arrange - Create test demand without sales statement
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = r2dbcEntityTemplate.insert(demand).block()!!

        // Act & Assert
        webTestClient
            .delete()
            .uri("/api/v1/billing/demands/${saved.demandId}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.demandId").isEqualTo(saved.demandId!!)
            .jsonPath("$.cancelled").isEqualTo(true)
    }

    @Test
    fun `should send sales statement via POST endpoint`() = runBlocking {
        // Arrange - Create test demand
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = r2dbcEntityTemplate.insert(demand).block()!!

        // Act & Assert
        webTestClient
            .post()
            .uri("/api/v1/billing/demands/${saved.demandId}/sales-statements")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.demandId").isEqualTo(saved.demandId!!)
            .jsonPath("$.slstmtNo").exists()
            .jsonPath("$.sentToErp").isEqualTo(true)
    }

    @Test
    fun `should return 404 when demand not found`() {
        // Arrange
        val nonExistentId = UUID.randomUUID().toString()

        // Act & Assert
        webTestClient
            .get()
            .uri("/api/v1/billing/demands/$nonExistentId")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 400 when cancelling already sent demand`() = runBlocking {
        // Arrange - Create demand with sales statement
        val demand = createTestDemand()
        demand.setAsNew()
        val saved = r2dbcEntityTemplate.insert(demand).block()!!

        // Send sales statement first
        saved.sendSalesStatement("SL-2025-001", "admin")
        r2dbcEntityTemplate.update(saved).block()

        // Act & Assert
        webTestClient
            .delete()
            .uri("/api/v1/billing/demands/${saved.demandId}")
            .exchange()
            .expectStatus().isBadRequest
    }

    private fun createTestDemand(): Demand {
        return Demand(
            demandId = UUID.randomUUID().toString(),
            demandDt = LocalDate.of(2025, 12, 31),
            custCd = "CUST001",
            demandStartDt = LocalDate.of(2025, 12, 1),
            demandStndDt = LocalDate.of(2025, 12, 31),
            stndPrice = BigDecimal("10000000"),
            supval = BigDecimal("9000000"),
            demandCharge = BigDecimal("9900000"),
            addtax = BigDecimal("900000"),
            dscntRate = BigDecimal("1.00"),
            creator = "admin"
        )
    }
}
