package com.idrsys.ailis.tst.adapter.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("TST Service API")
                    .version("1.0.0")
                    .description(
                        """
                        ## TST (Test) Service REST API Documentation
                        
                        이 서비스는 검사(Test) 관련 정보를 관리하는 MSA 서비스입니다.
                        
                        ### 주요 기능
                        - **Test Category**: 검사 카테고리 관리
                        - **Request Document**: 요청 문서 관리
                        - **Specimen**: 검체 및 검체 용기 관리
                        - **Test Reference**: 검사 항목 관리
                        - **Department Test Item**: 부서별 검사 항목 관리
                        - **Test Item**: 검사 아이템 및 관련 정보 관리
                        
                        ### 기술 스택
                        - Kotlin + Spring Boot 3.5.5
                        - Spring WebFlux (Reactive)
                        - Kotlin Coroutines
                        - Spring Data R2DBC + jOOQ
                        - PostgreSQL
                        """.trimIndent()
                    )
                    .contact(
                        Contact()
                            .name("IDRSYS")
                            .email("support@idrsys.com")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            )
    }
}
