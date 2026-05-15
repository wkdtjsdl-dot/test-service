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
import org.slf4j.LoggerFactory
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
    companion object {
        private val logger = LoggerFactory.getLogger(ReqServiceClient::class.java)
    }

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
     * @param custCds Customer codes to filter by (optional, null means all)
     * @return List of unbilled demand summaries
     */
    override suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        custCds: List<String>?,
    ): List<ReqServiceUnbilledDemandSummary> {
        val body = mapOf(
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString(),
            "custCds" to custCds
        )
        return try {
            client.post()
                .uri("/api/inner/rbs/tst-items/unbilled-demands")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .awaitBody<List<ReqServiceUnbilledDemandSummary>>()
        } catch (ex: Exception) {
            logger.error("req-service unbilled-demands 호출 실패: ${ex.message}", ex)
            emptyList()
        }
    }

    override suspend fun getClosedDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        custCds: List<String>?,
    ): List<ReqServiceUnbilledDemandSummary> {
        val body = mapOf(
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString(),
            "custCds" to custCds
        )
        return try {
            client.post()
                .uri("/api/inner/rbs/tst-items/closed-demands")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .awaitBody<List<ReqServiceUnbilledDemandSummary>>()
        } catch (ex: Exception) {
            logger.error("req-service closed-demands 호출 실패: ${ex.message}", ex)
            emptyList()
        }
    }

    /**
     * Get billing request details from req-service
     *
     * @param startDt Start date
     * @param endDt End date
     * @param custCds Customer codes (representative + constituent accounts)
     * @param closingCd Closing code (optional)
     * @return Flow of billing request details
     */
    override fun getBillingRequests(
        startDt: LocalDate,
        endDt: LocalDate,
        custCds: List<String>,
        closingCd: String?,
        tstReqDivCd: String?,
        crcyCd: String?,
    ): Flow<ReqServiceBillingRequestDetail> = flow {
        try {
            val details = client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/rbs/tst-items/billing-requests")
                    builder.queryParam("startDt", startDt.toString())
                    builder.queryParam("endDt", endDt.toString())
                    custCds.forEach { builder.queryParam("custCds", it) }
                    closingCd?.let { builder.queryParam("closingCd", it) }
                    tstReqDivCd?.let { builder.queryParam("tstReqDivCd", it) }
                    crcyCd?.let { builder.queryParam("crcyCd", it) }
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<ReqServiceBillingRequestDetail>>()

            details.forEach { emit(it) }
        } catch (ex: Exception) {
            logger.error("req-service billing-requests 호출 실패: custCds=$custCds, ${ex.message}", ex)
        }
    }

    /**
     * Update test item closing information
     */
    override suspend fun updateTstItemClosingInfo(
        custCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate,
        exrtId: Long?,
        stndExrt: BigDecimal?,
        closingMemo: String?,
        closingUser: String,
        tstReqDivCd: String?,
        crcyCd: String?,
    ): Int {
        val requestBody = mapOf(
            "custCds" to custCds,
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString(),
            "exrtId" to exrtId,
            "stndExrt" to stndExrt,
            "closingMemo" to closingMemo,
            "tstReqDivCd" to tstReqDivCd,
            "crcyCd" to crcyCd,
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
        custCds: List<String>,
        startDt: LocalDate,
        endDt: LocalDate,
        updater: String,
        tstReqDivCd: String?,
        crcyCd: String?,
    ): Int {
        val requestBody = mapOf(
            "custCds" to custCds,
            "startDt" to startDt.toString(),
            "endDt" to endDt.toString(),
            "tstReqDivCd" to tstReqDivCd,
            "crcyCd" to crcyCd,
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

    override suspend fun checkRequestsByCustCd(custCd: String): Int {
        return client.get()
            .uri("/api/inner/rbs/api-requests/{custCd}", custCd)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java)
                    .map { RuntimeException("External API error: $it") }
            }
            .awaitBody()
    }
}
