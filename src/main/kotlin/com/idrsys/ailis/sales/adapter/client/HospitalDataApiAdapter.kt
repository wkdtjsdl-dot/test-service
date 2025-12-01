package com.idrsys.ailis.sales.adapter.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.required.port.HospitalDataApiPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import kotlin.math.ceil

@Component
class HospitalDataApiAdapter(
    webClientBuilder: WebClient.Builder,
    @Value("\${hira.api.url.hosp-basis}") private val hospBasisUrl: String,
    @Value("\${hira.api.url.dgsbjt-info}") private val dgsbjtInfoUrl: String,
    @Value("\${hira.api.url.med-oft-info}") private val medOftInfoUrl: String,
    @Value("\${hira.api.url.close-at-info}") private val closeAtInfoUrl: String,
    @Value("\${hira.api.service-key}") private val serviceKey: String,
) : HospitalDataApiPort {
    private val webClient = webClientBuilder.build()
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
    }

    override fun fetchHospitalDataByAddrAndName(hospNm: String, sgguNm: String, emd: String): Flow<HospitalInfo> {
        val jacksonType = object : TypeReference<HospitalResponse<HospitalInfo>>() {}
        return webClient.get()
            .uri(hospBasisUrl) { builder ->
                builder.queryParam("ServiceKey", serviceKey)
                    .queryParam("yadmNm", hospNm)
                    .queryParam("sgguCdNm", sgguNm)
                    .queryParam("emdongNm", emd)
                    .queryParam("_type", "json")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
                    .map { json ->
                        try {
                            objectMapper.readValue(json, jacksonType) as HospitalResponse<HospitalInfo>
                        } catch (e: Exception) {
                            throw e
                        }
                    }
                    .flatMapMany { hospitalData ->
                        val items = hospitalData.response.body?.items?.item ?: emptyList()
                        Flux.fromIterable(items)
                    }
                    .asFlow()
    }

    override fun fetchMediSbjtData(ykiho: String): Flow<MediSbjtInfo> {
        val jacksonType = object : TypeReference<HospitalResponse<MediSbjtInfo>>() {}
        return webClient.get()
            .uri(dgsbjtInfoUrl) { builder ->
                builder.queryParam("ServiceKey", serviceKey)
                    .queryParam("ykiho", ykiho)
                    .queryParam("numOfRows", 100)
                    .queryParam("_type", "json")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .map { json ->
                try {
                    objectMapper.readValue(json, jacksonType) as HospitalResponse<MediSbjtInfo>
                } catch (e: Exception) {
                    throw e
                }
            }
            .flatMapMany { hospitalData ->
                val items = hospitalData.response.body?.items?.item ?: emptyList()
                Flux.fromIterable(items)
            }
            .asFlow()
    }

    override fun fetchDeviceData(ykiho: String): Flow<DeviceInfo> {
        val jacksonType = object : TypeReference<HospitalResponse<DeviceInfo>>() {}
        return webClient.get()
            .uri(medOftInfoUrl) { builder ->
                builder.queryParam("ServiceKey", serviceKey)
                    .queryParam("ykiho", ykiho)
                    .queryParam("numOfRows", 100)
                    .queryParam("_type", "json")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .map { json ->
                try {
                    objectMapper.readValue(json, jacksonType) as HospitalResponse<DeviceInfo>
                } catch (e: Exception) {
                    throw e
                }
            }
            .flatMapMany { hospitalData ->
                val items = hospitalData.response.body?.items?.item ?: emptyList()
                Flux.fromIterable(items)
            }
            .asFlow()
    }

    override fun fetchHospitalClosureData(crtrYm: String): Flow<HospitalClosureInfo> {
        return flow {
            var currentPage = 1
            var maxPage = Int.MAX_VALUE

            while (currentPage <= maxPage) {
                val jacksonType = object : TypeReference<HospitalResponse<HospitalClosureInfo>>() {}
                val response = webClient.get()
                    .uri(closeAtInfoUrl) { builder ->
                        builder.queryParam("ServiceKey", serviceKey)
                            .queryParam("pageNo", currentPage)
                            .queryParam("numOfRows", 100)
                            .queryParam("crtrYm", crtrYm)
                            .queryParam("yadmTp", 1)
                            .queryParam("opCloTp", 2)
                            .queryParam("_type", "json")
                            .build()
                    }
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .map { json ->
                        try {
                            objectMapper.readValue(json, jacksonType) as HospitalResponse<HospitalClosureInfo>
                        } catch (e: Exception) {
                            throw e
                        }
                    }
                    .awaitSingle()

                val body = response.response.body
                if (body == null) {
                    break
                }

                if (currentPage == 1) {
                    val totalCount = body.totalCount
                    maxPage = ceil(totalCount.toDouble() / body.numOfRows).toInt()
                }

                val items = body.items?.item ?: emptyList()
                if (items.isNotEmpty()) {
                    items.forEach { emit(it) }
                }

                if (items.isEmpty()) {
                    break
                }

                currentPage++
            }
        }
    }
}
