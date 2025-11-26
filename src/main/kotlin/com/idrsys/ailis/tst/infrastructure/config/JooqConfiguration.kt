package com.idrsys.ailis.tst.infrastructure.config

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.ParamType
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
@Configuration
class JooqConfiguration {

    @Bean
    @Primary
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
