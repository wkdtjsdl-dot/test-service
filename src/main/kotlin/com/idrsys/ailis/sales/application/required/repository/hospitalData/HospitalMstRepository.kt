package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.domain.model.HospitalMst

interface HospitalMstRepository {
    suspend fun save(hospitalMst: HospitalMst): HospitalMst
    suspend fun findById(careInstId: String): HospitalMst?
    suspend fun existsById(careInstId: String): Boolean
}