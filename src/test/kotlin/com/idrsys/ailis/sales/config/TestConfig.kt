package com.idrsys.ailis.sales.config

import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceBillingRequestDetail
import com.idrsys.ailis.sales.application.dto.response.inner.ReqServiceUnbilledDemandSummary
import com.idrsys.ailis.sales.application.required.external.ReqServicePort
import com.idrsys.reactive.excel.ReactiveExcelWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Integration test를 위한 공통 테스트 설정
 *
 * 테스트 환경에서는 실제 Excel 파일 생성이 필요하지 않으므로,
 * ReactiveExcelWriter를 Mock bean으로 제공합니다.
 */
@TestConfiguration
class TestConfig {

    /**
     * ReactiveExcelWriter Mock bean 제공
     *
     * @Primary: comm-on.reactive.excel.enabled=true로 실제 빈이 생성되어도
     *           테스트에서는 이 Mock bean을 우선 사용
     * RETURNS_DEEP_STUBS: 메서드 체이닝을 지원하여
     *                     별도의 stub 설정 없이도 사용 가능
     */
    @Bean
    @Primary
    fun reactiveExcelWriter(): ReactiveExcelWriter {
        return Mockito.mock(ReactiveExcelWriter::class.java, Mockito.RETURNS_DEEP_STUBS)
    }

    /**
     * ReqServicePort Fake implementation 제공
     *
     * 통합 테스트에서 req-service가 없으므로 Fake implementation으로 대체
     * 모든 메서드가 기본값(0 또는 빈 리스트)을 반환
     */
    @Bean
    @Primary
    fun reqServicePort(): ReqServicePort {
        return object : ReqServicePort {
            override suspend fun getUnbilledDemandSummary(
                startDt: LocalDate,
                endDt: LocalDate,
                custCd: String?
            ): List<ReqServiceUnbilledDemandSummary> = emptyList()

            override fun getBillingRequests(
                startDt: LocalDate,
                endDt: LocalDate,
                directAcctCd: String,
                closingCd: String?
            ): Flow<ReqServiceBillingRequestDetail> = emptyFlow()

            override suspend fun updateTstItemClosingInfo(
                directAcctCd: String,
                startDt: LocalDate,
                endDt: LocalDate,
                closingSupval: BigDecimal,
                closingAddtax: BigDecimal,
                closingDemandCharge: BigDecimal,
                exrtId: Long?,
                closingMemo: String?,
                closingUser: String
            ): Int = 0

            override suspend fun releaseTstItemClosingInfo(
                directAcctCd: String,
                startDt: LocalDate,
                endDt: LocalDate,
                updater: String
            ): Int = 0
        }
    }
}
