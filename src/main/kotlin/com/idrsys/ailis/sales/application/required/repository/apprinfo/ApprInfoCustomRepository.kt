package com.idrsys.ailis.sales.application.required.repository.apprinfo

import com.idrsys.ailis.sales.application.dto.query.ChargeApproveQuery
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.domain.model.ApprInfo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

/**
 * 결재정보 조회 Repository (Port)
 */
interface ApprInfoCustomRepository {
    /**
     * 결재정보번호로 결재선 전체 조회
     */
    fun findByApprInfoNo(apprInfoNo: Long): Flow<ApprInfo>

    /**
     * 결재정보번호와 순번으로 단건 조회
     */
    suspend fun findByApprInfoNoAndSeq(apprInfoNo: Long, apprSeq: Int): ApprInfo?

    /**
     * 현재 결재 대기 중인 항목 조회
     */
    suspend fun findPendingApprovalByChargeId(custChargeId: String): ApprInfo?

    /**
     * 승인 목록 조회 (페이징) - 고객수가 + 결재정보 조인
     * @param searchParam 검색 조건
     * @param userId 사용자 ID (역할 기반 필터링용)
     * @param userRole 사용자 직책 코드 (JP_TM, JP_PM, JP_TL, JP_DH, JP_P)
     * @param empNo 사원번호 (결재자 필터링용)
     * @param pageable 페이징 정보
     * @return 승인 조회용 DTO Flow
     */
    fun findApprovalCharges(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        userRole: String,
        empNo: String?,
        pageable: Pageable
    ): Flow<ChargeApproveQuery>

    /**
     * 승인 목록 카운트
     */
    suspend fun countApprovalCharges(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        userRole: String,
        empNo: String?
    ): Long

    /**
     * 승인 상세 조회 (결재선 포함)
     * @param custChargeId 고객수가 ID
     * @return 승인 상세 DTO + 결재선 목록
     */
    suspend fun findApprovalChargeWithLines(custChargeId: String): Pair<ChargeApproveQuery, List<ApprInfo>>?

    /**
     * 다음 결재 정보 번호 조회 (시퀀스)
     */
    suspend fun getNextApprInfoNo(): Long

    /**
     * 해당 사용자가 이미 결재선에 승인했는지 여부 확인
     */
    suspend fun hasUserApproved(apprInfoNo: Long, empNo: String): Boolean
}
