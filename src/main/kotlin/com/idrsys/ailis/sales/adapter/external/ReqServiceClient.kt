package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.required.port.ReqServicePort
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
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
     * @param custCd Customer code (optional)
     * @return List of unbilled demand summaries
     */
    override suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        custCd: String?
    ): List<ReqServiceUnbilledDemandSummary> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/rbs/tst-items/unbilled-demands")
                    builder.queryParam("startDt", startDt.toString())
                    builder.queryParam("endDt", endDt.toString())
                    custCd?.let { builder.queryParam("directAcctCd", it) }  // custCd를 직접거래처코드로 맵핑
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<ReqServiceUnbilledDemandSummary>>()
        } catch (ex: Exception) {
            emptyList()
        }
    }
}
