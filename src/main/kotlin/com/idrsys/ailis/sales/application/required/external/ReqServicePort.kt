package com.idrsys.ailis.sales.application.required.external

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceBillingRequestDetail
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

/**
 * req-service Inner API 클라이언트 인터페이스
 *
 * Clean Architecture의 Required Port로서 외부 서비스 호출을 추상화합니다.
 */
interface ReqServicePort {

    /**
     * Get unbilled demand summary from req-service
     *
     * @param startDt Start date
     * @param endDt End date
     * @param directAcctCds Direct account codes (optional, null means all)
     * @return List of unbilled demand summaries
     */
    suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        directAcctCds: List<String>? = null
    ): List<ReqServiceUnbilledDemandSummary>

    /**
     * Get billing request details from req-service
     *
     * @param startDt Start date
     * @param endDt End date
     * @param directAcctCd Direct account code (= custCd)
     * @param closingCd Closing code (optional)
     * @return Flow of billing request details
     */
    fun getBillingRequests(
        startDt: LocalDate,
        endDt: LocalDate,
        directAcctCd: String,
        closingCd: String? = null,
        demandType: String? = null
    ): Flow<ReqServiceBillingRequestDetail>

    /**
     * Update test item closing information
     *
     * @param directAcctCd Direct account code (= custCd)
     * @param startDt Start date
     * @param endDt End date
     * @param closingSupval Closing supply value
     * @param closingAddtax Closing additional tax
     * @param closingDemandCharge Closing demand charge
     * @param exrtId Exchange rate ID (optional)
     * @param closingMemo Closing memo (optional)
     * @param closingUser Closing user
     * @return Number of updated test items
     */
    suspend fun updateTstItemClosingInfo(
        directAcctCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        demandType: String,
        exrtId: Long? = null,
        stndExrt: BigDecimal? = null,
        closingMemo: String?,
        closingUser: String,
    ): Int

    /**
     * Release test item closing information
     *
     * @param directAcctCd Direct account code (= custCd)
     * @param startDt Start date
     * @param endDt End date
     * @param demandType 청구구분 (10: 일반, 30: 선수금)
     * @param updater Updater
     * @return Number of released test items
     */
    suspend fun releaseTstItemClosingInfo(
        directAcctCd: String,
        startDt: LocalDate,
        endDt: LocalDate,
        demandType: String,
        updater: String
    ): Int

    /**
     * 의뢰 검색
     * @param param 검색 조건
     * @return 의뢰 목록
     */
    suspend fun checkRequestsByCustCd(custCd: String): Int
}
