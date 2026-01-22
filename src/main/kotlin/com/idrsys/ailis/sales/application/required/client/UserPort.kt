package com.idrsys.ailis.sales.application.required.client

/**
 * base-service 사용자 정보 조회 Client (Port)
 */
interface UserPort {
    /**
     * 사용자 정보 조회
     * @param userId 사용자 ID
     * @return UserResponse (jbpoCd 포함)
     */
    suspend fun getUser(userId: String): UserResponse?
}

/**
 * base-service UserResponse DTO
 */
data class UserResponse(
    val userId: String,
    val userNm: String,
    val empNo: String?,
    val jbpoCd: String?,  // 직책코드
    val deptCd: String?,
)
