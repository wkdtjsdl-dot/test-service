package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalDevice

interface HospitalDeviceRepository {
    suspend fun save(hospitalDevice: HospitalDevice): HospitalDevice
    suspend fun findById(hospDeviceId: Long): HospitalDevice?
    suspend fun existsById(hospDeviceId: Long): Boolean
}