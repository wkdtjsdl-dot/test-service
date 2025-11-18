package com.idrsys.ailis.sales.application.required.repository.hospitalData

import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.domain.model.HospitalMst
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface HospitalMstCustomRepository {
    suspend fun countHospitalMstList(searchParam: HospitalDataSearchParam): Long

    fun findHospitalMstList(searchParam: HospitalDataSearchParam, pageable: Pageable?): Flow<HospitalMst>
}