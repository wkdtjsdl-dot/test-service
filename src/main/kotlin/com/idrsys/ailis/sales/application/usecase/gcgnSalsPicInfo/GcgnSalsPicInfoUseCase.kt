package com.idrsys.ailis.sales.application.usecase.gcgnSalsPicInfo

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GcgnSalsPicInfoUseCase {
    suspend fun getGcgnSalsPicInfoPage(searchParam: GcgnSalsPicInfoSearchParam, pageable: Pageable): Page<GcgnSalsPicInfoResponse>
    suspend fun getGcgnSalsPicInfoDetail(custMstId: String, gcgnSalsPicInfoId: Long): GcgnSalsPicInfoResponse
    suspend fun createGcgnSalsPicInfo(custMstId: String, command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse
    suspend fun updateGcgnSalsPicInfo(custMstId: String, gcgnSalsPicInfoId: Long, command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse
}
