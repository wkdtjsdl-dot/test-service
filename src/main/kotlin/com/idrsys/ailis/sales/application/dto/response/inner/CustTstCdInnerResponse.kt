package com.idrsys.ailis.sales.application.dto.response.inner

/**
 * 고객검사코드 매핑 Inner API 응답 DTO
 * req-service에서 sales-service의 고객검사코드 매핑 조회 시 사용
 */
data class CustTstCdInnerResponse(
    val custCd: String,
    val custTstCd: String,
    val tstCd: String?
)
