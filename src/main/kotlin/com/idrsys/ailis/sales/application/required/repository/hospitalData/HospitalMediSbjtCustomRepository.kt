package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import kotlinx.coroutines.flow.Flow

interface HospitalMediSbjtCustomRepository {
    fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt>
}