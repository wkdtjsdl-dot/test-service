package com.idrsys.ailis.tst.infrastructure.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * WebClient 설정
 * - base-service 등 다른 마이크로서비스 호출용
 */
@Configuration
class WebClientConfig {

    @Value("\${external.base-service.url:http://localhost:8080}")
    private lateinit var baseServiceUrl: String

    /**
     * base-service 호출용 WebClient
     */
    @Bean
    fun baseServiceWebClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .responseTimeout(Duration.ofSeconds(10))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(10, TimeUnit.SECONDS))
                    .addHandlerLast(WriteTimeoutHandler(10, TimeUnit.SECONDS))
            }

        return WebClient.builder()
            .baseUrl(baseServiceUrl)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}