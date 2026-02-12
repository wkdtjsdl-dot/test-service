package com.idrsys.ailis.sales.application.usecase.apprinfo

import com.idrsys.ailis.sales.application.dto.request.apprinfo.ApprInfoCreateRequest

interface ApprInfoInnerUseCase {
    /**
     * 다음 결재정보번호 시퀀스 조회
     */
    suspend fun getNextApprInfoNo(): Long

    /**
     * 결재라인 일괄 저장
     */
    suspend fun saveApprInfoBatch(requests: List<ApprInfoCreateRequest>)

    /**
     * 결재라인 삭제 (apprInfoNo로 전체 삭제)
     */
    suspend fun deleteByApprInfoNo(apprInfoNo: Long)
}
