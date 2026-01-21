package com.idrsys.ailis.tst.application.required.external

import com.idrsys.ailis.tst.application.dto.inner.TestItemKey
import com.idrsys.ailis.tst.application.dto.inner.TestItemStatusInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestInfo
import com.idrsys.ailis.tst.application.dto.inner.TestRequestKey

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
}
