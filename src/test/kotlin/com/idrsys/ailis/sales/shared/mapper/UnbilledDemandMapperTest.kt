package com.idrsys.ailis.sales.shared.mapper

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnbilledDemandMapperTest {

    @Test
    fun `should map bill publication flag independently from invoice email receipt flag`() {
        val summary = ReqServiceUnbilledDemandSummary(
            custCd = "CUST001",
            custNm = null,
            branchNm = null,
            stndPrice = BigDecimal("1000"),
            supval = BigDecimal("900"),
            addtax = BigDecimal("90"),
            demandCharge = BigDecimal("990"),
            requestCount = 1
        )

        val manualBillResponse = summary.toDemandResponse(
            searchStartDt = LocalDate.of(2026, 5, 1),
            searchEndDt = LocalDate.of(2026, 5, 31),
            billPublYn = false,
            invcRecpEmailYn = true
        )
        val electronicBillResponse = summary.toDemandResponse(
            searchStartDt = LocalDate.of(2026, 5, 1),
            searchEndDt = LocalDate.of(2026, 5, 31),
            billPublYn = true,
            invcRecpEmailYn = false
        )

        assertFalse(manualBillResponse.billPublYn)
        assertTrue(electronicBillResponse.billPublYn)
    }
}
