package com.idrsys.ailis.sales.adapter.client

import com.idrsys.ailis.sales.application.required.client.ApprovalLineClient
import com.idrsys.ailis.sales.application.required.client.ApprovalLineResponse
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import com.idrsys.ailis.sales.shared.constant.ChargeApproveErrorCode
import com.idrsys.web.exception.UserDefinedException
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
) : ApprovalLineClient {

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
            throw UserDefinedException(
                ChargeApproveErrorCode.APPROVAL_LINE_FETCH_FAILED_CODE,
                ChargeApproveErrorCode.APPROVAL_LINE_FETCH_FAILED_MESSAGE
            )
        }
    }
}
