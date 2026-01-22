package com.idrsys.ailis.sales.application.required.external

import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceRefItemsResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceStndChargeResponse
import com.idrsys.ailis.sales.application.dto.response.inner.TstServiceTstItemsResponse

/**
 * test-service 검사 관련 Client (Port)
 */
interface TstServicePort {
    /**
     * 검사 기준수가 조회
     * @param tstCd 검사코드
     * @return 기준수가 목록 (lowestCharge 포함)
     */
    suspend fun getStandardCharges(tstCd: String): List<TstServiceStndChargeResponse>

    /**
     * 검사코드로 검사항목 조회
     * @param tstCds 검사코드 목록
     * @return 검사항목 목록
     */
    suspend fun findTestItemByTestCode(tstCds: List<String>? = null): List<TstServiceTstItemsResponse>?

    /**
     * 참조항목코드로 참조항목 조회
     * @param refItemCds 참조항목코드 목록
     * @return 참조항목 목록
     */
    suspend fun findRefItemByRefItemCode(refItemCds: List<String>? = null): List<TstServiceRefItemsResponse>?

    /**
     * 전체 검사항목 조회
     * @return 검사항목 목록
     */
    suspend fun findAllTstItems(): List<TstServiceTstItemsResponse>?
}
