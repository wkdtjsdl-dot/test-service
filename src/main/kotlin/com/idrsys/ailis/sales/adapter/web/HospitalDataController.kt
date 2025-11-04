package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.usecase.hospitalData.HospitalDataUseCase
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/custs/{custMstId}")
@Tag(name = "HospitalDataController", description = "고객 병원 정보 Controller" )
class HospitalDataController(
    private val hospitalDataUseCase: HospitalDataUseCase
) {

    @GetMapping("/hosp-mst")
    @Operation(summary = "hospitalDetail" ,description = "고객 병원정보 요양기관 기본정보 상세 조회")
    suspend fun getHospitalDetailList(
        @PathVariable custMstId: String
    ): HospitalMst? {
        return hospitalDataUseCase.getHospitalMstDetail(custMstId)
    }

    @GetMapping("/hosp-device")
    @Operation(summary = "hospitalDevice" ,description = "고객 병원정보 요양기관 장비정보 목록")
    suspend fun getHospitalDevice(
        @PathVariable custMstId: String
    ): Flow<HospitalDevice> {
        return hospitalDataUseCase.getHospitalDevice(custMstId) ?: emptyFlow()
    }

    @GetMapping("/hosp-medi-sbjt")
    @Operation(summary = "hospitalMediSbjt" ,description = "고객 병원정보 요양기관 진료과목 목록")
    suspend fun getHospitalMediSbjt(
        @PathVariable custMstId: String
    ): Flow<HospitalMediSbjt> {
        return hospitalDataUseCase.getHospitalMediSbjt(custMstId) ?: emptyFlow()
    }
}