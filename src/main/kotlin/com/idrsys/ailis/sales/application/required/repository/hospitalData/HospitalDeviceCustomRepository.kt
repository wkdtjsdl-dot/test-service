package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalDevice
import kotlinx.coroutines.flow.Flow

interface HospitalDeviceCustomRepository {
    fun getHospitalDevice(careInstId: String): Flow<HospitalDevice>
}