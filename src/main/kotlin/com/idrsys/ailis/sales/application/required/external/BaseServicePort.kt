package com.idrsys.ailis.sales.application.required.external

import com.idrsys.ailis.sales.application.dto.request.attachedfile.AttachedFileGroupCreateCommand
import com.idrsys.ailis.sales.application.dto.response.inner.BaseAttachedFileGroupResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentDetailResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseDepartmentResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseSysCodeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.BaseUserResponse

interface BaseServicePort {
    /**
     * 사용자 정보 조회
     * @param userId 사용자 ID
     * @return BaseUserResponse (jbpoCd 포함)
     */
    suspend fun getUser(userId: String): BaseUserResponse?

    /**
     * 전체 사용자 목록 조회
     * @return 사용자 목록
     */
    suspend fun getUsers(): List<BaseUserResponse>?

    /**
     * 사용자 ID 목록으로 사용자 정보 조회
     * @param userIds 사용자 ID 목록
     * @return 사용자 목록
     */
    suspend fun getUsersByIds(userIds: List<String>): List<BaseUserResponse>?

    /**
     * 전체 부서 목록 조회
     * @return 부서 목록
     */
    suspend fun getDepartments(): List<BaseDepartmentDetailResponse>?

    /**
     * 부서 ID로 부서 정보 조회
     * @param departmentId 부서 ID
     * @return 부서 정보
     */
    suspend fun findDepartmentById(departmentId: String?): BaseDepartmentResponse?

    /**
     * 부서유형코드로 부서 정보 조회
     * @param deptTypeCd 부서유형코드
     * @return 부서 정보
     */
    suspend fun findDepartmentByDeptTypeCd(deptTypeCd: String?): BaseDepartmentResponse?

    /**
     * 상위 코드별 하위 시스템 코드 목록 조회
     * @param cateCds 카테고리 코드 목록
     * @return 카테고리별 시스템 코드 목록
     */
    suspend fun getChildrenSystemCodes(cateCds: List<String>): Map<String, List<BaseSysCodeResponse>>?

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

    /**
     * 첨부파일 그룹 생성
     */
    suspend fun saveAttachedFiles(
        command: AttachedFileGroupCreateCommand,
        creatorId: String
    ): BaseAttachedFileGroupResponse?

    suspend fun getDepartmentsByIds(deptCds: List<String>): List<BaseDepartmentResponse>?

    suspend fun getDeptCdsByBranchBcd(branchBcd: String): List<String>
}

/**
 * base-service ApprovalLineResponse DTO
 */
data class ApprovalLineResponse(
    val apprSeq: Int,
    val apprPersonEmpNo: String
)
