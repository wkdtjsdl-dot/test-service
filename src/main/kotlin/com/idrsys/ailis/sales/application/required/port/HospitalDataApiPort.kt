package com.idrsys.ailis.sales.application.required.port

import com.idrsys.ailis.sales.application.dto.response.DeviceInfo
import com.idrsys.ailis.sales.application.dto.response.HospitalClosureInfo
import com.idrsys.ailis.sales.application.dto.response.HospitalInfo
import com.idrsys.ailis.sales.application.dto.response.MediSbjtInfo
import kotlinx.coroutines.flow.Flow

interface HospitalDataApiPort {
    fun fetchHospitalDataByAddrAndName(hospNm: String, sgguNm: String, emd: String): Flow<HospitalInfo>

    fun fetchMediSbjtData(ykiho: String): Flow<MediSbjtInfo>

    fun fetchDeviceData(ykiho: String): Flow<DeviceInfo>

    fun fetchHospitalClosureData(crtrYm: String): Flow<HospitalClosureInfo>
}
