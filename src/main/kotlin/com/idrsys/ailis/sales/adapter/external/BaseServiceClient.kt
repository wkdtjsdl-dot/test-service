package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentDetailResponse
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import com.idrsys.ailis.sales.application.dto.response.inner.BaseUserResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseAttachedFileResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseAttachedFileGroupResponse
import com.idrsys.ailis.sales.application.dto.request.attachedfile.AttachedFileGroupCreateCommand
import com.idrsys.ailis.sales.application.dto.response.inner.BaseSysCodeResponse

@Component
class BaseServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) {
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "base-service" }?.endPoint
            ?: throw IllegalStateException("base-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    suspend fun getUser(userId: String?): BaseUserResponse? {
        return try {
            client.get()
                .uri("/api/inner/users/{userId}", userId)
                .retrieve()
                .awaitBody<BaseUserResponse>()
        } catch (ex: org.springframework.web.reactive.function.client.WebClientResponseException.NotFound) {
            null // 404 [ user Table과 join이 되지 않을 때 scs_cust_contract.cntr_pic_id 는 nullable이라 통과하게 생성함
        } catch (ex: Exception) {
            null
        }
    }
    suspend fun getUsers(): List<BaseUserResponse>? {
        return try {
            client.get()
                .uri("/api/inner/users")
                .retrieve()
                .awaitBody<List<BaseUserResponse>>()
        }catch (ex: Exception) {
            null
        }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<BaseUserResponse>? {
        if (userIds.isEmpty()) return emptyList()
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/users/byids")
                        .queryParam("ids", userIds.joinToString(","))
                        .build()
                }
                .retrieve()
                .awaitBody<List<BaseUserResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun getDepartments(): List<BaseDepartmentDetailResponse>? {
        return try {
            client.get()
                .uri("/api/inner/departments")
                .retrieve()
                .awaitBody<List<BaseDepartmentDetailResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun findDepartmentById(departmentId: String?): BaseDepartmentResponse? {
        return try {
            client.get()
                .uri("/api/inner/departments/{departmentId}", departmentId)
                .retrieve()
                .awaitBody<BaseDepartmentResponse>()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun findDepartmentByDeptTypeCd(deptTypeCd: String?): BaseDepartmentResponse? {
            return try {
                client.get()
                    .uri("/api/inner/departments/by-deptType", deptTypeCd)
                    .retrieve()
                    .awaitBody<BaseDepartmentResponse>()
            } catch (ex: Exception){
                null
            }
    }

    // ========== 시스템 코드 관련 메서드 ==========

    /**
     * 상위 코드별 하위 시스템 코드 목록 조회
     */
    suspend fun getChildrenSystemCodes(cateCds: List<String>): Map<String, List<BaseSysCodeResponse>>? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/system-codes/by-cates")
                        .queryParam("cateCds", cateCds.joinToString(","))
                        .build()
                }
                .retrieve()
                .awaitBody<Map<String, List<BaseSysCodeResponse>>>()
        } catch (ex: Exception) {
            null
        }
    }

    // ========== 첨부파일 관련 메서드 ==========

    /**
     * 첨부파일 상세 조회
     */
    suspend fun getAttachedFile(attachedFileId: String): BaseAttachedFileResponse? {
        return try {
            client.get()
                .uri("/api/inner/attachments/{id}", attachedFileId)
                .retrieve()
                .awaitBody<BaseAttachedFileResponse>()
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * 여러 첨부파일 조회
     */
    suspend fun getAttachedFilesByIds(ids: List<String>): List<BaseAttachedFileResponse>? {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/attachments/byids")
                        .queryParam("ids", ids.joinToString(","))
                        .build()
                }
                .retrieve()
                .awaitBody<List<BaseAttachedFileResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * 첨부파일 그룹 생성 (기존 파일 재사용 + 새 파일 추가)
     */
    suspend fun saveAttachedFiles(
        request: AttachedFileGroupCreateCommand,
        creatorId: String
    ): BaseAttachedFileGroupResponse? {
        return try {
            client.post()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/attachments")
                        .queryParam("creatorId", creatorId)
                        .build()
                }
                .bodyValue(request)
                .retrieve()
                .awaitBody<BaseAttachedFileGroupResponse>()
        } catch (ex: Exception) {
            null
        }
    }
}