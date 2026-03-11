package com.idrsys.ailis.sales.application.usecase.gcgnSalsPicInfo

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalaPicInfoAutoSearchParam
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoAutoResponse
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GcgnSalsPicInfoUseCase {
    suspend fun getGcgnSalsPicInfoPage(searchParam: GcgnSalsPicInfoSearchParam, pageable: Pageable): Page<GcgnSalsPicInfoResponse>
    suspend fun getGcgnSalsPicInfoDetail(gcgnSalsPicInfoId: Long): GcgnSalsPicInfoResponse
    suspend fun createGcgnSalsPicInfo(command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse
    suspend fun updateGcgnSalsPicInfo(gcgnSalsPicInfoId: Long, command: GcgnSalsPicInfoCommand, adminId: String): GcgnSalsPicInfoResponse
    suspend fun deleteGcgnSalsPicInfo(gcgnSalsPicInfoId: Long)
    suspend fun getSalsPicAutoCompleteList(searchParam: GcgnSalaPicInfoAutoSearchParam): List<GcgnSalsPicInfoAutoResponse>
}
