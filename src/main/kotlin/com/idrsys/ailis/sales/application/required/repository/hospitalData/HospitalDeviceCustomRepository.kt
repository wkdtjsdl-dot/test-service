package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalDevice
import kotlinx.coroutines.flow.Flow

interface HospitalDeviceCustomRepository {
    fun getHospitalDevice(careInstId: String): Flow<HospitalDevice>
    suspend fun findByCareInstIdAndOftCd(careInstId: String, oftCd: String): HospitalDevice?
    suspend fun findAllDeviceCdsByCareInstId(careInstId: String): List<String>
    suspend fun deleteByCareInstIdAndDeviceCdNotIn(careInstId: String, deviceCds: List<String>): Int
}