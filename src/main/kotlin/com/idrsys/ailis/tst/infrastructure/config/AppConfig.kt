package com.idrsys.ailis.tst.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "uri")
class AppConfig {
    lateinit var services: List<ServiceEndpoint>

    data class ServiceEndpoint(
        val name: String,
        val endPoint: String
    )
}