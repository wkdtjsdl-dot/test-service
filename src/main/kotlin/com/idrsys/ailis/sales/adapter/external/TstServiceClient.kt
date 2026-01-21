package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceRefItemsResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceStndChargeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceUnbilledDemandPage
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate

@Component
class TstServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) {
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "tst-service" }?.endPoint
            ?: throw IllegalStateException("tst-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    suspend fun findTestItemByTestCode(
        tstCds: List<String>? = null
    ): List<TstServiceTstItemsResponse>? {
        if (tstCds.isNullOrEmpty()) return emptyList()

        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/bts/item")
                    builder.queryParam("cds", tstCds.joinToString(","))
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<TstServiceTstItemsResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun findRefItemByRefItemCode(
        refItemCds: List<String>? = null
    ): List<TstServiceRefItemsResponse>? {
        if (refItemCds.isNullOrEmpty()) return emptyList()

        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/bbs/tst-ref")
                    refItemCds.forEach { refItemCd ->
                        builder.queryParam("refItemCds", refItemCd)
                    }
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<TstServiceRefItemsResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun findAllTstItems(): List<TstServiceTstItemsResponse>? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/bts/item/all")
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<TstServiceTstItemsResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun getStandardCharges(tstCd: String): List<TstServiceStndChargeResponse>? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/inner/bts/item/stnd-charge")
                        .queryParam("tstCd", tstCd)
                        .build()
                }
                .retrieve()
                .awaitBody<List<TstServiceStndChargeResponse>>()
        } catch (ex: Exception) {
            null
        }
    }
}
