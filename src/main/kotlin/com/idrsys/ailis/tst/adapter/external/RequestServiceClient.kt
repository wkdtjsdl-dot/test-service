package com.idrsys.ailis.tst.adapter.external

import com.idrsys.ailis.tst.application.dto.inner.TestItemKey
import com.idrsys.ailis.tst.application.dto.inner.TestItemStatusInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestKey
import com.idrsys.ailis.tst.application.dto.inner.TstRequestDetailResponse
import com.idrsys.ailis.tst.application.dto.inner.toTestRequestInfo
import com.idrsys.ailis.tst.application.required.ReqServiceClient
import com.idrsys.ailis.tst.infrastructure.config.AppConfig
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

/**
 * req-service Inner API 클라이언트
 * - 검사 의뢰 정보 조회
 * - 검사 항목 상태 조회
 */
@Component
class RequestServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : ReqServiceClient {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "req-service" }?.endPoint
            ?: throw IllegalStateException("req-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    /**
     * 검사 의뢰 정보 일괄 조회
     * @param keys 의뢰일자 + 의뢰번호 키 목록
     * @return 검사 의뢰 정보 목록 (환자명, 거래처, 병원, 의뢰상태 등)
     */
    override suspend fun getTestRequestsByKeys(keys: List<TestRequestKey>): List<TestRequestInfo> {
        if (keys.isEmpty()) {
            return emptyList()
        }

        return try {
            // TestRequestKey를 TstReqeustSearchParam 형태로 변환
            val searchParams = keys.map { key ->
                mapOf(
                    "tstReqDt" to key.tstReqDt.toString(),
                    "tstReqNo" to key.tstReqNo
                )
            }

            val responses = client.post()
                .uri("/api/inner/rbs/requests")
                .bodyValue(searchParams)
                .retrieve()
                .bodyToMono<List<TstRequestDetailResponse>>()
                .awaitSingleOrNull() ?: emptyList()

            // TstRequestDetailResponse를 TestRequestInfo로 변환
            responses.map { it.toTestRequestInfo() }
        } catch (e: Exception) {
            logger.error("Failed to fetch test requests from req-service: keys=$keys", e)
            emptyList()
        }
    }

    /**
     * 검사 항목 상태 일괄 조회
     * @param keys 의뢰일자 + 의뢰번호 + 검사코드 키 목록
     * @return 검사 항목 상태 정보 목록 (검사상태1, 검사상태2)
     */
    override suspend fun getTestItemsStatus(keys: List<TestItemKey>): List<TestItemStatusInfo> {
        if (keys.isEmpty()) {
            return emptyList()
        }

        return try {
            // 먼저 의뢰 정보를 조회하여 검사 항목 정보를 추출
            val requestKeys = keys.map { TestRequestKey(it.tstReqDt, it.tstReqNo) }.distinct()

            val searchParams = requestKeys.map { key ->
                mapOf(
                    "tstReqDt" to key.tstReqDt.toString(),
                    "tstReqNo" to key.tstReqNo
                )
            }

            val responses = client.post()
                .uri("/api/inner/rbs/requests")
                .bodyValue(searchParams)
                .retrieve()
                .bodyToMono<List<TstRequestDetailResponse>>()
                .awaitSingleOrNull() ?: emptyList()

            // TstRequestDetailResponse에서 검사 항목 상태 정보 추출
            responses.flatMap { response ->
                response.tstItems?.mapNotNull { tstItem ->
                    // keys에 포함된 검사코드만 필터링
                    val matchingKey = keys.find {
                        it.tstReqDt == response.tstReqDt &&
                        it.tstReqNo == response.tstReqNo &&
                        it.tstCd == tstItem.tstCd
                    }

                    if (matchingKey != null) {
                        TestItemStatusInfo(
                            tstReqDt = response.tstReqDt,
                            tstReqNo = response.tstReqNo,
                            tstCd = tstItem.tstCd,
                            tstStat1Cd = response.tstReqStatCd ?: "UNKNOWN",
                            tstStat2Cd = "UNKNOWN" // TstItemResponse에 상태 정보가 없으므로 기본값 사용
                        )
                    } else {
                        null
                    }
                } ?: emptyList()
            }
        } catch (e: Exception) {
            logger.error("Failed to fetch test items status from req-service: keys=$keys", e)
            emptyList()
        }
    }
}