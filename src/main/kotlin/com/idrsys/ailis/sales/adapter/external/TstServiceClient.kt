package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceRefItemsResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceStndChargeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse
import com.idrsys.ailis.sales.application.required.external.TstServicePort
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToFlow

@Component
class TstServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
): TstServicePort {
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "tst-service" }?.endPoint
            ?: throw IllegalStateException("tst-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    override suspend fun findTestItemByTestCode(
        tstCds: List<String>?
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

    override suspend fun findRefItemByRefItemCode(
        refItemCds: List<String>?
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

    override suspend fun findAllTstItems(useYn: Boolean?, reqPossYn: Boolean?): List<TstServiceTstItemsResponse>? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder.path("/api/inner/bts/item/all")
                    if (useYn != null) builder.queryParam("useYn", useYn)
                    if (reqPossYn != null) builder.queryParam("reqPossYn", reqPossYn)
                    builder.build()
                }
                .retrieve()
                .awaitBody<List<TstServiceTstItemsResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun getStandardCharges(tstCd: String): List<TstServiceStndChargeResponse> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/inner/bts/item/stnd-charge")
                        .queryParam("tstCd", tstCd)
                        .build()
                }
                .retrieve()
                .bodyToFlow<TstServiceStndChargeResponse>()
                .toList()
        } catch (ex: Exception) {
            emptyList()
        }
    }
}
