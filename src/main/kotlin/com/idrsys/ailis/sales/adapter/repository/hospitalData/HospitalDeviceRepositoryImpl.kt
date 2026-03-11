package com.idrsys.ailis.sales.adapter.repository.hospitalData

import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalDeviceRepository
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalDeviceDataImpl : CoroutineCrudRepository<HospitalDevice, Long>

@Repository
class HospitalDeviceRepositoryImpl(
    private val hospitalDeviceRepository: HospitalDeviceDataImpl,
) : HospitalDeviceRepository {

    override suspend fun save(hospitalDevice: HospitalDevice): HospitalDevice {
        return hospitalDeviceRepository.save(hospitalDevice)
    }

    override suspend fun findById(hospDeviceId: Long): HospitalDevice? {
        return hospitalDeviceRepository.findById(hospDeviceId)
    }

    override suspend fun existsById(hospDeviceId: Long): Boolean {
        return hospitalDeviceRepository.existsById(hospDeviceId)
    }

}
