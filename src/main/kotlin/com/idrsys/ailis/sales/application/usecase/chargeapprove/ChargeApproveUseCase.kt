package com.idrsys.ailis.sales.application.usecase.chargeapprove

import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveActionCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveRequestCommand
import com.idrsys.ailis.sales.application.dto.request.chargeapprove.ChargeApproveSearchParam
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
     * 반려
     */
    suspend fun reject(command: ChargeApproveActionCommand, userId: String): ChargeApproveResponse

    /**
     * 승인 목록 조회 (페이징)
     */
    suspend fun getApprovalPage(
        searchParam: ChargeApproveSearchParam,
        userId: String,
        pageable: Pageable
    ): Page<ChargeApproveResponse>

    /**
     * 승인 목록 조회 (전체)
     */
    suspend fun getApprovals(
        searchParam: ChargeApproveSearchParam,
        userId: String
    ): List<ChargeApproveResponse>

    /**
     * 승인 상세 조회
     */
    suspend fun getApprovalDetail(custChargeId: String, userId: String): ChargeApproveResponse
}
