package com.idrsys.ailis.sales.adapter.external

import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentDetailResponse
import com.idrsys.ailis.sales.infrastructure.config.AppConfig
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import com.idrsys.ailis.sales.application.dto.response.inner.BaseUserResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentResponse

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
}