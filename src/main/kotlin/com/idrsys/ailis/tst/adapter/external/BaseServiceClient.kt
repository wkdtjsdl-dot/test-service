package com.idrsys.ailis.tst.adapter.external

import com.idrsys.ailis.tst.application.dto.DepartmentSimpleDto
import com.idrsys.ailis.tst.infrastructure.config.AppConfig
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

/**
 * base-service Inner API 클라이언트
 * - 부서, 시스템 코드 등 base-service의 데이터 조회
 */
@Component
class BaseServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "base-service" }?.endPoint
            ?: throw IllegalStateException("base-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    /**
     * 부서 코드 목록으로 부서 정보 조회
     * @param deptCds 부서 코드 목록
     * @return 부서 코드 -> 부서명 맵
     */
    suspend fun getDepartmentsByDeptCds(deptCds: List<String>): Map<String, String> {
        if (deptCds.isEmpty()) {
            return emptyMap()
        }

        return try {
            val deptCdsParam = deptCds.joinToString(",")

            val departments = client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/departments/byDeptCds")
                        .queryParam("deptCds", deptCdsParam)
                        .build()
                }
                .retrieve()
                .bodyToMono<List<DepartmentSimpleDto>>()
                .awaitSingleOrNull() ?: emptyList()

            // 부서 코드 -> 부서명 맵으로 변환
            departments.associate { it.deptCd to it.deptNm }
        } catch (e: Exception) {
            logger.error("Failed to fetch departments from base-service: deptCds=$deptCds", e)
            emptyMap()
        }
    }

     /**
     * 단일 부서 코드로 부서명 조회
     * @param deptCd 부서 코드
     * @return 부서명 (조회 실패 시 null)
     */
    suspend fun getDepartmentName(deptCd: String): String? {
        return getDepartmentsByDeptCds(listOf(deptCd))[deptCd]
    }

    // TODO: 향후 필요 시 추가 Inner API 메서드 구현
    // - getSystemCodes(): 시스템 코드 조회
    // - getAttachedFiles(): 첨부파일 정보 조회
    // 등등
}