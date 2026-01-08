package com.idrsys.ailis.sales.config

import com.idrsys.reactive.excel.ReactiveExcelWriter
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

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
}