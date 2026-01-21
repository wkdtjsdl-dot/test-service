package com.idrsys.ailis.sales.adapter.client

import com.idrsys.ailis.sales.application.required.client.ApprovalLinePort
import com.idrsys.ailis.sales.application.required.client.ApprovalLineResponse
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow

/**
 * base-service 결재선 조회 Client 구현체
 */
@Component
class ApprovalLineClientImpl(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : ApprovalLinePort {

    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "base-service" }?.endPoint
            ?: throw IllegalStateException("base-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    override suspend fun getApprovalLines(
        userId: String,
        apprDocTypeCd: String,
        apprDocDtlNo: String
    ): List<ApprovalLineResponse> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/apprLine")
                        .queryParam("userId", userId)
                        .queryParam("apprDocTypeCd", apprDocTypeCd)
                        .queryParam("apprDocDtlNo", apprDocDtlNo)
                        .build()
                }
                .retrieve()
                .bodyToFlow<ApprovalLineResponse>()
                .toList()
        } catch (ex: Exception) {
            // API 호출 실패 시 빈 리스트 반환, 서비스 레이어에서 처리
            emptyList()
        }
    }
}
