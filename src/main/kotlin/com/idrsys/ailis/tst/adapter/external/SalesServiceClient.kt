package com.idrsys.ailis.tst.adapter.external

import com.idrsys.ailis.tst.application.dto.inner.CustCdNmResponse
import com.idrsys.ailis.tst.application.required.external.SalesServicePort
import com.idrsys.ailis.tst.infrastructure.config.AppConfig
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

/**
 * sales-service Inner API 클라이언트
 * - 병원(고객) 등 sales-service의 데이터 조회
 */
@Component
class SalesServiceClient(
  webClientBuilder: WebClient.Builder,
  appConfig: AppConfig
): SalesServicePort {
  private val logger = LoggerFactory.getLogger(javaClass)
  private val client: WebClient

  init {
    val serviceEndpoint = appConfig.services.find { it.name == "sales-service" }?.endPoint
      ?: throw IllegalStateException("sales-service endpoint not found in configuration")
    client = webClientBuilder.baseUrl(serviceEndpoint).build()
  }

  /**
   * 고객 코드 목록으로 고객명 조회
   * @param custCds 고객 코드 목록
   * @return 고객 코드 -> 고객명 맵
   */
  override suspend fun findCustNmByCustCd(custCds: List<String>): Map<String, CustCdNmResponse> {
    if (custCds.isEmpty()) {
      return emptyMap()
    }

    return try {
      return client.post()
        .uri("/api/inner/custs/custNms")
        .bodyValue(custCds)
        .retrieve()
        .bodyToMono<Map<String, CustCdNmResponse>>()
        .awaitSingleOrNull() ?: emptyMap()

    } catch (e: Exception) {
      logger.error("Failed to fetch custs from sales-service: custCds=$custCds", e)
      emptyMap()
    }
  }
}