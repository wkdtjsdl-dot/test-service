package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceBillingRequestDetail
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceClosingReleaseResponse
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceClosingResponse
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.required.external.ReqServicePort
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import com.idrsys.web.exception.UserDefinedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal
import java.time.LocalDate

@Component
class ReqServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : ReqServicePort {
    final private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "req-service" }?.endPoint
            ?: throw IllegalStateException("req-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    /**
     * Get unbilled demand summary from req-service
     *
     * @param startDt Start date
     * @param endDt End date
     * @param directAcctCds Direct account codes (optional, null means all)
     * @return List of unbilled demand summaries
     */
    override suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        directAcctCds: List<String>?
    ): List<ReqServiceUnbilledDemandSummary> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/rbs/tst-items/unbilled-demands")
                    builder.queryParam("startDt", startDt.toString())
                    builder.queryParam("endDt", endDt.toString())
                    directAcctCds?.takeIf { it.isNotEmpty() }?.let {
                        builder.queryParam("directAcctCds", it.joinToString(","))
                    }
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<ReqServiceUnbilledDemandSummary>>()
        } catch (ex: Exception) {
            emptyList()
        }
    }

    /**
     * Get billing request details from req-service
     *
     * @param startDt Start date
     * @param endDt End date
     * @param directAcctCd Direct account code (= custCd)
     * @param closingCd Closing code (optional)
     * @return Flow of billing request details
     */
    override fun getBillingRequests(
        startDt: LocalDate,
        endDt: LocalDate,
        directAcctCd: String,
        closingCd: String?
    ): Flow<ReqServiceBillingRequestDetail> = flow {
        try {
            val details = client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/rbs/tst-items/billing-requests")
                    builder.queryParam("startDt", startDt.toString())
                    builder.queryParam("endDt", endDt.toString())
                    builder.queryParam("directAcctCd", directAcctCd)
                    closingCd?.let { builder.queryParam("closingCd", it) }
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<ReqServiceBillingRequestDetail>>()

            details.forEach { emit(it) }
        } catch (ex: Exception) {
            // Error handling: emit nothing on failure
        }
    }

    /**
     * Update test item closing information
     */
    override suspend fun updateTstItemClosingInfo(
        directAcctCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        closingSupval: BigDecimal,
        closingAddtax: BigDecimal,
        closingDemandCharge: BigDecimal,
        exrtId: Long?,
        stndExrt: BigDecimal?,
        closingMemo: String?,
        closingUser: String,
    ): Int {
        val requestBody = mapOf(
            "directAcctCd" to directAcctCd,
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString(),
            "exrtId" to exrtId,
            "stndExrt" to stndExrt,
            "closingMemo" to closingMemo
        )

        return try {
            val response = client.post()
                .uri("/api/inner/rbs/tst-items/closing")
                .header("X-Closing-User", closingUser)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .awaitBody<ReqServiceClosingResponse>()

            response.updatedCount
        } catch (ex: Exception) {
            throw UserDefinedException(
                "REQ_SERVICE_ERROR",
                "검사항목 마감 처리 중 오류가 발생했습니다: ${ex.message}"
            )
        }
    }

    /**
     * Release test item closing information
     */
    override suspend fun releaseTstItemClosingInfo(
        directAcctCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        updater: String
    ): Int {
        val requestBody = mapOf(
            "directAcctCd" to directAcctCd,
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString()
        )

        return try {
            val response = client.post()
                .uri("/api/inner/rbs/tst-items/closing/release")
                .header("X-Updater", updater)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .awaitBody<ReqServiceClosingReleaseResponse>()

            response.releasedCount
        } catch (ex: Exception) {
            throw UserDefinedException(
                "REQ_SERVICE_ERROR",
                "검사항목 마감 해제 중 오류가 발생했습니다: ${ex.message}"
            )
        }
    }
}
