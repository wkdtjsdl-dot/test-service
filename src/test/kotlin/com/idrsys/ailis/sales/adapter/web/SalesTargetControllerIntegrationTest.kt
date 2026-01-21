package com.idrsys.ailis.sales.adapter.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.idrsys.ailis.sales.application.dto.request.salesTarget.MonthlyTargetItem
import com.idrsys.ailis.sales.application.dto.request.salesTarget.SalesTargetSaveRequest
import com.idrsys.ailis.sales.config.JwtTestHelper
import com.idrsys.ailis.sales.config.TestConfig
import com.idrsys.ailis.sales.config.TestDatabaseConfig
import com.idrsys.ailis.sales.domain.model.SalesTarget
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.http.MediaType
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.reactive.TransactionalOperator
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * SalesTarget Controller Integration Test
 *
 * Testing REST API endpoints with WebTestClient
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig::class, TestDatabaseConfig::class)
@TestPropertySource(
    properties = [
        "spring.cloud.config.enabled=false",
        "spring.config.import=",
        "spring.profiles.active=test"
    ]
)
class SalesTargetControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var r2dbcEntityTemplate: R2dbcEntityTemplate

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    @Autowired
    private lateinit var transactionalOperator: TransactionalOperator

    @Value("\${jwt.secretkey}")
    private lateinit var jwtSecretKey: String

    private lateinit var accessToken: String

    companion object {
        private const val TEST_CUST_CD = "TEST-CUST-001"
        private const val TEST_CUST_NM = "테스트병원"
        private const val TEST_DIRECT_ACCT_CD = "DIRECT001"
    }

    @BeforeEach
    fun setUp(): Unit = runBlocking {
        // Generate JWT access token for test
        accessToken = JwtTestHelper.createAccessToken(
            adminId = "test-admin",
            secretKey = jwtSecretKey
        )

        // Clean up test data
        databaseClient.sql("DELETE FROM sales_scm.sbl_sales_target").then().block()
        databaseClient.sql("DELETE FROM sales_scm.scs_cust_mst").then().block()

        // Create test customer
        createTestCust()
    }

    @Test
    fun `should get sales targets via GET endpoint`(): Unit = runBlocking {
        // Arrange - Create test data
        val salesTarget = createTestSalesTarget(TEST_CUST_CD)
        salesTarget.setAsNew()
        r2dbcEntityTemplate.insert(salesTarget).block()

        // Act & Assert
        webTestClient
            .get()
            .uri("/api/sales/targets?year=2026")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.content[0].year").isEqualTo(2026)
            .jsonPath("$.content[0].custCd").isEqualTo(TEST_CUST_CD)
            .jsonPath("$.content[0].salesTeamCd").isEqualTo("TS-G")
    }

    @Test
    fun `should get sales targets with directAcctCd filter`(): Unit = runBlocking {
        // Arrange - Create test data
        val salesTarget = createTestSalesTarget(TEST_CUST_CD)
        salesTarget.setAsNew()
        r2dbcEntityTemplate.insert(salesTarget).block()

        // Act & Assert - directAcctCd 필터가 있는 경우
        webTestClient
            .get()
            .uri("/api/sales/targets?year=2026&directAcctCd=$TEST_DIRECT_ACCT_CD")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.content[0].custCd").isEqualTo(TEST_CUST_CD)
    }

    @Test
    fun `should return empty list when no matching data`() {
        // Act & Assert
        webTestClient
            .get()
            .uri("/api/sales/targets?year=2099")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.content").isEmpty
    }

    @Test
    fun `should get sales target details via GET endpoint`(): Unit = runBlocking {
        // Arrange - Create test data for multiple months
        for (month in 1..3) {
            val salesTarget = SalesTarget(
                salesTargetId = UUID.randomUUID().toString(),
                custCd = TEST_CUST_CD,
                salesYear = "2026",
                salesMonth = month.toString().padStart(2, '0'),
                salsTeamCd = "TS-G",
                monthSalesTargetAmt = BigDecimal("10000000"),
                pastYearMonthSalesAmt = BigDecimal("8000000"),
                creator = "admin",
                createDtime = LocalDateTime.now(),
                updater = "admin",
                updateDtime = LocalDateTime.now()
            )
            salesTarget.setAsNew()
            r2dbcEntityTemplate.insert(salesTarget).block()
        }

        // Act & Assert
        webTestClient
            .get()
            .uri("/api/sales/targets/detail?year=2026&custCd=$TEST_CUST_CD")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.content.length()").isEqualTo(3)
            .jsonPath("$.content[0].custCd").isEqualTo(TEST_CUST_CD)
            .jsonPath("$.content[0].month").isEqualTo(1)
    }

    @Test
    fun `should save sales targets via POST endpoint`(): Unit = runBlocking {
        // Arrange
        val request = SalesTargetSaveRequest(
            year = 2026,
            custCd = TEST_CUST_CD,
            monthlyTargets = listOf(
                MonthlyTargetItem(
                    month = 1,
                    salsTeamCd = "TS-G",
                    monthlyTarget = BigDecimal("10000000"),
                    prevYearSales = BigDecimal("8000000")
                ),
                MonthlyTargetItem(
                    month = 2,
                    salsTeamCd = "TS-G",
                    monthlyTarget = BigDecimal("11000000"),
                    prevYearSales = BigDecimal("8500000")
                )
            )
        )

        // Act & Assert
        webTestClient
            .post()
            .uri("/api/sales/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .cookie("accessToken", accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.content").isArray
            .jsonPath("$.content.length()").isEqualTo(2)
            .jsonPath("$.content[0].custCd").isEqualTo(TEST_CUST_CD)
    }

    @Test
    fun `should update existing sales target via POST endpoint`(): Unit = runBlocking {
        // Arrange - Create existing sales target
        val existingTarget = SalesTarget(
            salesTargetId = UUID.randomUUID().toString(),
            custCd = TEST_CUST_CD,
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            monthSalesTargetAmt = BigDecimal("10000000"),
            pastYearMonthSalesAmt = BigDecimal("8000000"),
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
        existingTarget.setAsNew()
        r2dbcEntityTemplate.insert(existingTarget).block()

        // Create update request with different amount
        val request = SalesTargetSaveRequest(
            year = 2026,
            custCd = TEST_CUST_CD,
            monthlyTargets = listOf(
                MonthlyTargetItem(
                    month = 1,
                    salsTeamCd = "TS-G",
                    monthlyTarget = BigDecimal("15000000"),
                    prevYearSales = BigDecimal("9000000")
                )
            )
        )

        // Act & Assert
        webTestClient
            .post()
            .uri("/api/sales/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .cookie("accessToken", accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.content[0].monthlyTarget").isEqualTo(15000000)
            .jsonPath("$.content[0].prevYearSales").isEqualTo(9000000)
    }

    @Test
    fun `should return 500 when saving without JWT token`() {
        // Arrange
        val request = SalesTargetSaveRequest(
            year = 2026,
            custCd = TEST_CUST_CD,
            monthlyTargets = listOf(
                MonthlyTargetItem(
                    month = 1,
                    salsTeamCd = "TS-G",
                    monthlyTarget = BigDecimal("10000000")
                )
            )
        )

        // Act & Assert
        // JWT 토큰이 없으면 JwtAuthorizationArgumentResolver에서 IllegalStateException 발생
        // "인증 Token이 유효하지 않습니다. 재 로그인하시기 바랍니다."
        webTestClient
            .post()
            .uri("/api/sales/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError
    }

    private fun createTestSalesTarget(custCd: String): SalesTarget {
        return SalesTarget(
            salesTargetId = UUID.randomUUID().toString(),
            custCd = custCd,
            salesYear = "2026",
            salesMonth = "01",
            salsTeamCd = "TS-G",
            monthSalesTargetAmt = BigDecimal("10000000"),
            pastYearMonthSalesAmt = BigDecimal("8000000"),
            creator = "admin",
            createDtime = LocalDateTime.now(),
            updater = "admin",
            updateDtime = LocalDateTime.now()
        )
    }

    private suspend fun createTestCust() {
        val now = LocalDateTime.now()
        databaseClient.sql(
            """
            INSERT INTO sales_scm.scs_cust_mst (cust_mst_id, cust_cd, cust_nm, direct_acct_cd, creator, create_dtime, updater, update_dtime)
            VALUES (:custMstId, :custCd, :custNm, :directAcctCd, :creator, :createDtime, :updater, :updateDtime)
            """
        )
            .bind("custMstId", UUID.randomUUID().toString())
            .bind("custCd", TEST_CUST_CD)
            .bind("custNm", TEST_CUST_NM)
            .bind("directAcctCd", TEST_DIRECT_ACCT_CD)
            .bind("creator", "admin")
            .bind("createDtime", now)
            .bind("updater", "admin")
            .bind("updateDtime", now)
            .then()
            .awaitFirstOrNull()
    }
}
