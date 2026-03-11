package com.idrsys.ailis.sales.infrastructure.config

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.ParamType
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * jOOQ Configuration for PostgreSQL (Production/Dev environments)
 *
 * @ConditionalOnMissingBean을 사용하여 테스트 환경에서
 * TestDatabaseConfig의 dslContext bean이 있으면 이 bean을 생성하지 않습니다.
 */
@Configuration
class JooqConfiguration {

    @Bean
    @ConditionalOnMissingBean(DSLContext::class)
    fun dslContext(): DSLContext {
        return createDslContext()
    }

    private fun createDslContext(): DSLContext {
        val settings = Settings()
            .withRenderSchema(true)
            .withExecuteLogging(false)
            .withParamType(ParamType.NAMED)

        return DSL.using(SQLDialect.POSTGRES, settings)
    }

    @Bean
    fun jooqSettings(): Settings {
        return Settings()
            .withRenderSchema(true)
            .withExecuteLogging(false)
    }
}
