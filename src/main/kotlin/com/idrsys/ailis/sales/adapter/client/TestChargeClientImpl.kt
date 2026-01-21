package com.idrsys.ailis.sales.adapter.client

import com.idrsys.ailis.sales.application.required.client.StandardChargeResponse
import com.idrsys.ailis.sales.application.required.client.TestChargePort
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow

/**
 * test-service 검사 기준수가 조회 Client 구현체
 */
@Component
class TestChargeClientImpl(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : TestChargePort {

    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "tst-service" }?.endPoint
            ?: throw IllegalStateException("test-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    override suspend fun getStandardCharges(tstCd: String): List<StandardChargeResponse> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/bts/item/stnd-charge")
                        .queryParam("tstCd", tstCd)
                        .build()
                }
                .retrieve()
                .bodyToFlow<StandardChargeResponse>()
                .toList()
        } catch (ex: Exception) {
            // API 호출 실패 시 빈 리스트 반환, 서비스 레이어에서 처리
            emptyList()
        }
    }
}
