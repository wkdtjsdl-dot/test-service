package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.request.attachedfile.AttachedFileGroupCreateCommand
import com.idrsys.ailis.sales.application.dto.response.inner.BaseAttachedFileGroupResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentDetailResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseSysCodeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseUserResponse
import org.springframework.http.MediaType
import com.idrsys.ailis.sales.application.required.external.ApprovalLineResponse
import com.idrsys.ailis.sales.application.required.external.BaseServicePort
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToFlow

@Component
class BaseServiceClient(
    webClientBuilder: WebClient.Builder,
    appConfig: AppConfig
) : BaseServicePort {
    private val client: WebClient

    init {
        val serviceEndpoint = appConfig.services.find { it.name == "base-service" }?.endPoint
            ?: throw IllegalStateException("base-service endpoint not found in configuration")
        client = webClientBuilder.baseUrl(serviceEndpoint).build()
    }

    override suspend fun getUser(userId: String): BaseUserResponse? {
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
    override suspend fun getUsers(): List<BaseUserResponse>? {
        return try {
            client.get()
                .uri("/api/inner/users")
                .retrieve()
                .awaitBody<List<BaseUserResponse>>()
        }catch (ex: Exception) {
            null
        }
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<BaseUserResponse>? {
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

    override suspend fun getDepartments(): List<BaseDepartmentDetailResponse>? {
        return try {
            client.get()
                .uri("/api/inner/departments")
                .retrieve()
                .awaitBody<List<BaseDepartmentDetailResponse>>()
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun findDepartmentById(departmentId: String?): BaseDepartmentResponse? {
        return try {
            client.get()
                .uri("/api/inner/departments/{departmentId}", departmentId)
                .retrieve()
                .awaitBody<BaseDepartmentResponse>()
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun findDepartmentByDeptTypeCd(deptTypeCd: String?): BaseDepartmentResponse? {
            return try {
                client.get()
                    .uri("/api/inner/departments/by-deptType", deptTypeCd)
                    .retrieve()
                    .awaitBody<BaseDepartmentResponse>()
            } catch (ex: Exception){
                null
            }
    }

    override suspend fun getDepartmentsByIds(deptCds: List<String>): List<BaseDepartmentResponse>? {
        if (deptCds.isEmpty()) return emptyList()
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/departments/byDeptCds")
                        .queryParam("deptCds", deptCds)
                        .build()
                }
                .retrieve()
                .awaitBody<List<BaseDepartmentResponse>>()
        } catch (ex: Exception) {
            println("Error fetching departments: ${ex.message}") // 로그 확인용
            null
        }
    }

    // ========== 시스템 코드 관련 메서드 ==========

    /**
     * 상위 코드별 하위 시스템 코드 목록 조회
     */
    override suspend fun getChildrenSystemCodes(cateCds: List<String>): Map<String, List<BaseSysCodeResponse>>? {
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
     * 첨부파일 그룹 생성
     */
    override suspend fun saveAttachedFiles(
        command: AttachedFileGroupCreateCommand,
        creatorId: String
    ): BaseAttachedFileGroupResponse? {
        return try {
            client.post()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/attachments")
                        .queryParam("creatorId", creatorId)
                        .build()
                }
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .retrieve()
                .awaitBody<BaseAttachedFileGroupResponse>()
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun getDeptCdsByBranchBcd(branchBcd: String): List<String> {
        return try {
            client.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/api/inner/departments/byBranchBcd")
                        .queryParam("branchBcd", branchBcd)
                        .build()
                }
                .retrieve()
                .awaitBody<List<String>>()
        } catch (ex: Exception) {
            emptyList()
        }
    }

    // ========== 결재라인 관련 메서드 ==========

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
