package com.idrsys.ailis.sales.infrastructure.config

import com.idrsys.common.kor2dbc.config.EnableCommOnMstSlvDataSource
import com.idrsys.common.kor2dbc.datasource.CustomDatabaseProperties
import com.idrsys.common.kor2dbc.datasource.DatabaseProperties
import com.idrsys.common.kor2dbc.generator.UuidIdGeneratorCallback
import org.apache.poi.ss.formula.functions.T
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

/**
 * Base Configuration for Production/Dev environments
 *
 * Master/Slave 데이터소스 설정을 포함합니다.
 * 테스트 환경에서는 database.enable-master-slave=false로 설정하여 비활성화합니다.
 * TODO - 현재는 database.enable-master-salve 환경 설정이 없음. r2dbc bean도 auto config 설정을 환경설정으로 변경한 후 @Profile 제거
 */
@Configuration
//@ConditionalOnProperty(
//    prefix = "database",
//    name = ["enable-master-slave"],
//    havingValue = "true",
//    matchIfMissing = true  // property가 없으면 활성화 (기본값: 운영/개발 환경)
//)
@Profile("!test")
@EnableCommOnMstSlvDataSource
@EnableR2dbcRepositories(
    basePackages = ["com.idrsys.ailis.sales.adapter.repository"],
    entityOperationsRef = "r2dbcEntityTemplate"
)
class DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.r2dbc")
    fun databaseProperties(): DatabaseProperties = DatabaseProperties()

    @Bean
    @ConfigurationProperties(prefix = "database")
    fun customDatabaseProperties(): CustomDatabaseProperties = CustomDatabaseProperties()

    @Bean
    fun beforeSaveCallback(): BeforeSaveCallback<T> = UuidIdGeneratorCallback() // UUID 저장시 필요
}
