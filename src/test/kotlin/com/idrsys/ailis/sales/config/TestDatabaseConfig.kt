package com.idrsys.ailis.sales.config

import com.idrsys.common.kor2dbc.generator.UuidIdGeneratorCallback
import org.apache.poi.ss.formula.functions.T
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameCase
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Test Database Configuration
 *
 * 테스트 환경에서는 Master/Slave 설정 없이 단순한 H2 in-memory DB를 사용합니다.
 * - BaseConfig는 database.enable-master-slave=false로 비활성화됩니다.
 * - 이 Configuration에서 H2 전용 설정을 제공합니다.
 * - spring.main.allow-bean-definition-overriding=true로 bean override를 허용합니다.
 */
@TestConfiguration
@Profile("test")
@EnableR2dbcRepositories(
    basePackages = ["com.idrsys.ailis.sales.adapter.repository"]
)
class TestDatabaseConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encodingId = "bcrypt"
        val encoders = mapOf(
            encodingId to BCryptPasswordEncoder()
        )
        return DelegatingPasswordEncoder(encodingId, encoders)
    }

    @Bean
    fun beforeSaveCallback(): BeforeSaveCallback<T> = UuidIdGeneratorCallback()

    /**
     * jOOQ DSLContext for H2 Tests
     *
     * H2 테스트 환경에서 스키마 이름과 테이블 이름을 렌더링할 때:
     * - withRenderSchema(true): 스키마 이름 포함 (sales_scm.sbl_demand)
     * - withRenderQuotedNames(NEVER): 따옴표 제거 (대소문자 구분 안 함)
     * - withRenderNameCase(LOWER): 모든 식별자를 소문자로 렌더링
     *
     * PostgreSQL은 따옴표가 없으면 자동으로 소문자로 변환하지만,
     * H2는 따옴표가 있으면 대소문자를 엄격히 구분합니다.
     * 따라서 따옴표를 제거하고 소문자로 렌더링하여 H2와 호환되도록 합니다.
     *
     * @Primary 어노테이션과 spring.main.allow-bean-definition-overriding=true로
     * JooqConfiguration의 bean을 override합니다.
     */
    @Bean
    @Primary
    fun dslContext(): org.jooq.DSLContext {
        val settings = Settings()
            .withRenderSchema(true)  // 스키마 이름 렌더링 활성화
            .withRenderQuotedNames(RenderQuotedNames.NEVER)  // 따옴표 렌더링 비활성화
            .withRenderNameCase(RenderNameCase.LOWER)  // 식별자를 소문자로 렌더링

        val configuration = DefaultConfiguration()
            .set(SQLDialect.H2)
            .set(settings)

        return DSL.using(configuration)
    }
}
