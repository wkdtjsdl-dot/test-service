package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandPage
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate

@Component
class ReqServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) {
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
     * @param page Page number
     * @param size Page size
     * @return Page of unbilled demand summaries
     */
    suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        custCd: String? = null,
        page: Int = 0,
        size: Int = 15
    ): ReqServiceUnbilledDemandPage? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/rbs/tst-item/unbilled-demands")
                    builder.queryParam("startDt", startDt.toString())
                    builder.queryParam("endDt", endDt.toString())
                    custCd?.let { builder.queryParam("custCd", it) }
                    builder.queryParam("page", page)
                    builder.queryParam("size", size)
                    builder.build()
                }
                .retrieve()
                .awaitBody<ReqServiceUnbilledDemandPage>()
        } catch (ex: Exception) {
            null
        }
    }
}
