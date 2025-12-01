package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import kotlinx.coroutines.flow.Flow

interface HospitalMediSbjtCustomRepository {
    fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt>
    suspend fun findByCareInstIdAndDgsbjtCd(careInstId: String, dgsbjtCd: String): HospitalMediSbjt?
    fun findAllMediSbjtCdsByCareInstId(careInstId: String): Flow<String>
    suspend fun deleteByCareInstIdAndMediSbjtCdNotIn(careInstId: String, mediSbjtCds: List<String>): Int
}