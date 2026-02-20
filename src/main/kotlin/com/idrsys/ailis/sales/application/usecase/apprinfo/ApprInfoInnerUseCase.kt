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

    /**
     * 결재정보번호 + 결재순번으로 결재자 userId 조회
     * @param apprInfoNo 결재정보번호
     * @param apprSeq 결재순번
     * @return 결재자 userId (apprPersonEmpNo), 없으면 null
     */
    suspend fun findApproverByApprInfoNoAndSeq(apprInfoNo: Long, apprSeq: Int): String?

    /**
     * 내 결재 필터용 appr_info_no 목록 조회
     * @param userId 사용자 ID
     * @param apprDocTypeCds 결재문서유형코드 목록 (예: APDC_ET, APDC_CN)
     * @return appr_info_no 목록
     */
    suspend fun findMyApprovalInfoNos(userId: String, apprDocTypeCds: List<String>): List<Long>

    /**
     * 결재선 승인 처리 (APST_W → APST_C)
     * @param apprInfoNo 결재정보번호
     * @param apprSeq 결재순번
     * @param apprMemo 결재메모
     * @param userId 승인자 ID
     */
    suspend fun approveApprInfo(apprInfoNo: Long, apprSeq: Int, apprMemo: String?, userId: String)

    /**
     * 다음 결재자 존재 여부 확인
     * @param apprInfoNo 결재정보번호
     * @param currentSeq 현재 결재순번
     * @return 다음 결재자가 있으면 true
     */
    suspend fun hasNextApprover(apprInfoNo: Long, currentSeq: Int): Boolean
}
