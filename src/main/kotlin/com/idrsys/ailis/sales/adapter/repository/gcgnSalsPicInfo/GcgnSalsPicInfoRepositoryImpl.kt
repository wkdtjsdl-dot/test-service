package com.idrsys.ailis.sales.adapter.repository.gcgnSalsPicInfo

import com.idrsys.ailis.sales.application.required.repository.gcgnSalsPicInfo.GcgnSalsPicInfoRepository
import com.idrsys.ailis.sales.domain.model.GcgnSalsPicInfo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GcgnSalsPicInfoDataRepository : CoroutineCrudRepository<GcgnSalsPicInfo, Long>

@Repository
class GcgnSalsPicInfoRepositoryImpl(
    private val gcgnSalsPicInfoDataRepository: GcgnSalsPicInfoDataRepository
) : GcgnSalsPicInfoRepository {
    override suspend fun save(gcgnSalsPicInfo: GcgnSalsPicInfo): GcgnSalsPicInfo {
        return gcgnSalsPicInfoDataRepository.save(gcgnSalsPicInfo)
    }

    override suspend fun findById(id: Long): GcgnSalsPicInfo? {
        return gcgnSalsPicInfoDataRepository.findById(id)
    }

    override suspend fun deleteById(id: Long) {
        gcgnSalsPicInfoDataRepository.deleteById(id)
    }
}
