package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt

interface HospitalMediSbjtRepository {
    suspend fun save(hospitalMediSbjt: HospitalMediSbjt): HospitalMediSbjt
    suspend fun findById(hospMediSbjtId: Long): HospitalMediSbjt?
    suspend fun existsById(hospMediSbjtId: Long): Boolean
}