package com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo

import com.idrsys.ailis.sales.application.dto.query.GcgnSalsPicInfoQuery
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface GcgnSalsPicInfoCustomRepository {
    fun findGcgnSalsPicInfos(searchParam: GcgnSalsPicInfoSearchParam, pageable: Pageable?): Flow<GcgnSalsPicInfoQuery>
    suspend fun countGcgnSalsPicInfos(searchParam: GcgnSalsPicInfoSearchParam): Long
    suspend fun findGcgnSalsPicInfoById(gcgnSalsPicInfoId: Long): GcgnSalsPicInfoQuery?
    suspend fun findDomainById(id: Long): GcgnSalsPicInfo?
}
