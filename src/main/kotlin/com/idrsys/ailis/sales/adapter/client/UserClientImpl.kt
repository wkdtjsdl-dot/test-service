package com.idrsys.ailis.sales.adapter.client

import com.idrsys.ailis.sales.application.required.client.UserClient
import com.idrsys.ailis.sales.application.required.client.UserResponse
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

/**
 * base-service 사용자 정보 조회 Client 구현체
 */
@Component
class UserClientImpl(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : UserClient {

    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "base-service" }?.endPoint
            ?: throw IllegalStateException("base-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    override suspend fun getUser(userId: String): UserResponse? {
        return try {
            client.get()
                .uri("/api/inner/users/{userId}", userId)
                .retrieve()
                .awaitBody<UserResponse>()
        } catch (ex: Exception) {
            null
        }
    }
}
