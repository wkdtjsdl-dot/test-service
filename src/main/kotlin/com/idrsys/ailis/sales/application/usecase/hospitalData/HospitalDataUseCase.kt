package com.idrsys.ailis.sales.application.usecase.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import kotlinx.coroutines.flow.Flow

interface HospitalDataUseCase {
    suspend fun getHospitalMstDetail(careInstId: String): HospitalMst?

    suspend fun getHospitalDevice(careInstId: String): Flow<HospitalDevice>?

    suspend fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt>?
}