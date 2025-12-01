package com.idrsys.ailis.sales.application.usecase.hospitalData

import com.idrsys.ailis.sales.application.dto.request.hospitalData.HospitalDataSearchParam
import com.idrsys.ailis.sales.application.dto.response.HospitalMstResponse
import com.idrsys.ailis.sales.domain.model.HospitalDevice
import com.idrsys.ailis.sales.domain.model.HospitalMediSbjt
import com.idrsys.ailis.sales.domain.model.HospitalMst
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HospitalDataUseCase {
    suspend fun getHospitalMstListPage(searchParam: HospitalDataSearchParam, pageable: Pageable): Page<HospitalMstResponse>

    suspend fun getHospitalMstDetail(careInstId: String): HospitalMst?

    suspend fun getHospitalDevice(careInstId: String): Flow<HospitalDevice>

    suspend fun getHospitalMediSbjt(careInstId: String): Flow<HospitalMediSbjt>

    fun executeHospitalDataSynchronization(): Boolean
}