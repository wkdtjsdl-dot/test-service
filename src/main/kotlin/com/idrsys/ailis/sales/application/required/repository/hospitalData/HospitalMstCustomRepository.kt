package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.domain.model.HospitalMst
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface HospitalMstCustomRepository {
    suspend fun countHospitalMstList(searchParam: HospitalDataSearchParam): Long
    suspend fun findByEncpCareInstNo(encpCareInstNo: String): HospitalMst?
    fun findAllEncpCareInstNo(): Flow<String>
    fun findHospitalMstList(searchParam: HospitalDataSearchParam, pageable: Pageable?): Flow<HospitalMst>
    suspend fun findByCareInstId(careInstId: String): HospitalMst?
}