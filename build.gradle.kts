plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("kapt") version "1.9.25"
    id("nu.studer.jooq") version "8.2"
}

group = "com.idrsys.ailis"
version = "0.0.1-SNAPSHOT"
description = "sales-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    // 기존 레포지토리들...
    maven {
        url = uri("${rootProject.projectDir}/lib")
    }
}

extra["springCloudVersion"] = "2025.0.0"

configurations.implementation {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation("com.sap.conn.jco:sapjco3:3.1.6") // SAP
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("com.idrsys.comm-on:ko-r2dbc-lib:0.0.2")
    implementation("com.idrsys.comm-on:kowebflux-lib:0.0.4")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.8.13")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.13")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql")
    jooqGenerator("org.postgresql:postgresql")
    implementation("org.jooq:jooq:3.20.7")
    implementation("software.amazon.awssdk:s3:2.29.39")
    implementation("org.springframework.security:spring-security-crypto")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    environment("CONFIG_SERVER", "")
    systemProperty("spring.cloud.config.enabled", "false")
    systemProperty("spring.config.import", "")
}

jooq {
    version.set("3.20.7")
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.ERROR
                onError = org.jooq.meta.jaxb.OnError.SILENT
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = System.getenv("JOOQ_URL") ?: property("jooq.url") as String
                    user = System.getenv("JOOQ_USER") ?: property("jooq.user") as String
                    password = System.getenv("JOOQ_PASSWORD") ?: property("jooq.password") as String
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "sales_scm"
                        includes = ".*"
                        excludes = ""
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = false
                        isDaos = false
                        isValidationAnnotations = true
                        isSpringAnnotations = true
                        isKeys = false
                        isRelations = false
                        isIndexes = false
                    }
                    target.apply {
                        packageName = "com.idrsys.ailis.sales.generated.jooq"
                        directory = "src/main/generated"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
