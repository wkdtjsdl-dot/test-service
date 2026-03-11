package com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo

import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo

interface GcgnSalsPicInfoRepository {
    suspend fun save(gcgnSalsPicInfo: GcgnSalsPicInfo): GcgnSalsPicInfo
    suspend fun findById(id: Long): GcgnSalsPicInfo?
    suspend fun deleteById(id: Long)
}
