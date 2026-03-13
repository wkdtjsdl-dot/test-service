package com.idrsys.ailis.tst.application.required.external

import com.idrsys.ailis.tst.application.dto.inner.*
import java.time.LocalDate

/**
 * req-service Inner API 클라이언트 인터페이스
 */
interface ReqServicePort {

    /**
     * 검사 의뢰 정보 일괄 조회
     *
     * @param keys 의뢰일자 + 의뢰번호 키 목록
     * @return 검사 의뢰 정보 목록 (환자명, 거래처, 병원, 의뢰상태 등)
     */
    suspend fun getTestRequestsByKeys(keys: List<TestRequestKey>): List<TestRequestInfo>

    /**
     * 검사 항목 상태 일괄 조회
     *
     * @param keys 의뢰일자 + 의뢰번호 + 검사코드 키 목록
     * @return 검사 항목 상태 정보 목록 (검사상태1, 검사상태2)
     */
    suspend fun getTestItemsStatus(keys: List<TestItemKey>): List<TestItemStatusInfo>

    /**
     * 의뢰일자, 의뢰번호로 검사항목 목록 조회
     *
     * @param tstReqDt 의뢰일자
     * @param tstReqNo 의뢰번호
     * @return 검사 항목 상태 정보 목록 (검사상태1, 검사상태2)
     */
    suspend fun updateTstItemStatus(tstReqDt: LocalDate, tstReqNo: Long, tstCd: String, statusCd: String, updater: String): Boolean
}
