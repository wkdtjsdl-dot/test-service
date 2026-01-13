package com.idrsys.ailis.sales.application.required.port

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
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
     * @param custCd Customer code (optional)
     * @return List of unbilled demand summaries
     */
    suspend fun getUnbilledDemandSummary(
        startDt: LocalDate,
        endDt: LocalDate,
        custCd: String? = null
    ): List<ReqServiceUnbilledDemandSummary>
}