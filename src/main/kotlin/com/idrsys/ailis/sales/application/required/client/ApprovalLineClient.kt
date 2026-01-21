package com.idrsys.ailis.sales.application.required.client

/**
 * base-service 결재선 조회 Client (Port)
 */
interface ApprovalLineClient {
    /**
     * 결재선 조회
     * @param userId 사용자 ID
     * @param apprDocTypeCd 결재문서유형코드 (예: "APDC_CC")
     * @param apprDocDtlNo 결재문서상세번호 ("0" or "1")
     * @return 결재선 목록
     */
    suspend fun getApprovalLines(
        userId: String,
        apprDocTypeCd: String,
        apprDocDtlNo: String
    ): List<ApprovalLineResponse>
}

/**
 * base-service ApprovalLineResponse DTO
 */
data class ApprovalLineResponse(
    val apprSeq: Int,
    val apprPersonEmpNo: String
)
