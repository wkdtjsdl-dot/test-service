package com.idrsys.ailis.tst.infrastructure.config

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


@Configuration
@EnableCommOnMstSlvDataSource
@EnableR2dbcRepositories(
    basePackages = ["com.idrsys.ailis.tst.adapter.repository"],
    entityOperationsRef = "r2dbcEntityTemplate"
)
class TstConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.r2dbc")
    fun databaseProperties(): DatabaseProperties = DatabaseProperties()

    @Bean
    @ConfigurationProperties(prefix = "database")
    fun customDatabaseProperties(): CustomDatabaseProperties = CustomDatabaseProperties()

    @Bean
    fun beforeSaveCallback(): BeforeSaveCallback<T> = UuidIdGeneratorCallback()
}
