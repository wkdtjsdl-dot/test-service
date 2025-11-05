package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalDeviceCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMediSbjtCustomRepository
import com.idrsys.ailis.sales.application.required.repository.hospitalData.HospitalMstRepository
import com.idrsys.ailis.sales.application.usecase.hospitalData.HospitalDataUseCase
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class HospitalDataService(
    private val hospitalMstRepository: HospitalMstRepository,
    private val hospitalDeviceCustomRepository: HospitalDeviceCustomRepository,
    private val hospitalMediSbjtCustomRepository: HospitalMediSbjtCustomRepository
) : HospitalDataUseCase {
    override suspend fun getHospitalMstDetail(careInstId: String): HospitalMst {
        return hospitalMstRepository.findById(careInstId)
            ?: throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
    }

    override suspend fun getHospitalDevice(careInstId: String): Flow<HospitalDevice> {
        if (!hospitalMstRepository.existsById(careInstId)) {
            throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
        }
        return hospitalDeviceCustomRepository.getHospitalDevice(careInstId)
    }

    override suspend fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt> {
        if (!hospitalMstRepository.existsById(careInstId)) {
            throw IllegalArgumentException("존재하지 않는 ID입니다: $careInstId")
        }
        return hospitalMediSbjtCustomRepository.getHospitalMediSbjt(careInstId)
    }
}