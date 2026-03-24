package com.idrsys.ailis.sales.application.usecase.chargeapprove

import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
import com.idrsys.ailis.sales.application.dto.response.ApprovalLineInfo
import com.idrsys.ailis.sales.application.dto.response.ChargeApproveResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * 고객수가 승인 UseCase
 */
interface ChargeApproveUseCase {
    /**
     * 승인 요청
     */
    suspend fun requestApproval(command: ChargeApproveRequestCommand, userId: String): ChargeApproveResponse

    /**
     * 승인
     */
    suspend fun approve(command: ChargeApproveActionCommand, userId: String): ChargeApproveResponse

    /**
     * 고객 수가 삭제 (상태에 따라 분기: 임시저장 건 삭제 / 결재중인 건 반려)
     */
    suspend fun deleteCharge(custChargeId: String, userId: String)

    /**
     * 고객 수가 반려 (결재중인 건만)
     * - 반려 시 scs_cust_charge와 scs_appr_info 전체 삭제
     */
    suspend fun rejectCharge(custChargeId: String, userId: String)

    /**
     * 승인 목록 조회 (페이징)
     */
    suspend fun getApprovalPage(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        roleCodes: List<String>,
        pageable: Pageable
    ): Page<ChargeApproveResponse>

    /**
     * 승인 목록 조회 (전체)
     */
    suspend fun getApprovals(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        roleCodes: List<String>
    ): List<ChargeApproveResponse>

    /**
     * 승인 상세 조회
     */
    suspend fun getApprovalDetail(custChargeId: String, userId: String): ChargeApproveResponse

    /**
     * 결재정보번호로 결재라인 조회
     * @param apprInfoNo 결재정보번호
     * @return 결재라인 목록
     */
    suspend fun findApprovalLinesByApprInfoNo(apprInfoNo: Long): List<ApprovalLineInfo>

}
