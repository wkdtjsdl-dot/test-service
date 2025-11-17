package com.idrsys.ailis.sales.infrastructure.config

import com.idrsys.common.kor2dbc.config.EnableCommOnMstSlvDataSource
import com.idrsys.common.kor2dbc.datasource.CustomDatabaseProperties
import com.idrsys.common.kor2dbc.datasource.DatabaseProperties
import com.idrsys.common.kor2dbc.generator.UuidIdGeneratorCallback
import org.apache.poi.ss.formula.functions.T
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableCommOnMstSlvDataSource
@EnableR2dbcRepositories(
    basePackages = ["com.idrsys.ailis.sales.adapter.repository"],
    entityOperationsRef = "r2dbcEntityTemplate"
)
class BaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.r2dbc")
    fun databaseProperties(): DatabaseProperties = DatabaseProperties()

    @Bean
    @ConfigurationProperties(prefix = "database")
    fun customDatabaseProperties(): CustomDatabaseProperties = CustomDatabaseProperties()

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encodingId = "bcrypt"
        val encoders = mapOf(
            encodingId to BCryptPasswordEncoder()
        )

        return DelegatingPasswordEncoder(encodingId, encoders)
    }

    @Bean
    fun beforeSaveCallback(): BeforeSaveCallback<T> = UuidIdGeneratorCallback() // UUID 저장시 필요
}
