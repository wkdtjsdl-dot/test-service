package com.idrsys.ailis.tst.adapter.external

import com.idrsys.ailis.tst.application.dto.DepartmentSimpleDto
import com.idrsys.ailis.tst.application.dto.inner.SysCodeResponse
import com.idrsys.ailis.tst.application.dto.inner.SysCodeSearchParam
import com.idrsys.ailis.tst.application.required.external.BaseServicePort
import com.idrsys.ailis.tst.infrastructure.config.AppConfig
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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
) : BaseServicePort {
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
    override suspend fun getDepartmentsByDeptCds(deptCds: List<String>): Map<String, String> {
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
    override suspend fun getDepartmentName(deptCd: String): String? {
        return getDepartmentsByDeptCds(listOf(deptCd))[deptCd]
    }


    // JSON 응답을 받아내기 위한 임시 DTO
    data class PageResponse<T>(
        val content: List<T> = listOf(),
        val number: Int = 0,
        val size: Int = 20,
        val totalElements: Long = 0
    )

    override suspend fun getSysCodeList(param: SysCodeSearchParam): Page<SysCodeResponse> {
        return try {
            val response = client.get()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/inner/system-codes")
                    param.cateCd?.let { uriBuilder.queryParam("cateCd", it) }
                    uriBuilder.queryParam("useYn", true)
                        .queryParam("page", 0)
                        .queryParam("size", 20)
                        .build()
                }
                .retrieve()
                // 인터페이스(Page)가 아닌 구현 클래스(PageResponse)로 받기
                .bodyToMono<PageResponse<SysCodeResponse>>()
                .awaitSingle()

            // PageImpl을 생성하여 인터페이스 타입으로 반환
            // 생성자 파라미터: (실제 데이터 리스트, 페이지 정보, 전체 데이터 개수)
            PageImpl(
                response.content,
                PageRequest.of(response.number, response.size),
                response.totalElements
            )

        } catch (e: Exception) {
            logger.error("Failed to fetch system codes: param=$param", e)
            Page.empty()
        }
    }

    override suspend fun getSysCodesByCateCd(cateCd: String): List<SysCodeResponse> {
        val page = getSysCodeList(
            SysCodeSearchParam(
                cateCd = cateCd
            )
        )
        return page.content
    }

    // 향후 필요 시 추가 Inner API 메서드 구현
    // - getAttachedFiles(): 첨부파일 정보 조회
    // 등등
}