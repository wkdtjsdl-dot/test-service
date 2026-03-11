package com.idrsys.ailis.sales.adapter.repository.ifConfInfo

import com.idrsys.ailis.sales.application.required.repository.ifConfInfo.IfConfInfoRepository
import com.idrsys.ailis.sales.domain.model.IfConfInfo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IfConfInfoDataRepository : CoroutineCrudRepository<IfConfInfo, String> {
    suspend fun deleteByIfCustInfoId(ifCustInfoId: String)
}

@Repository
class IfConfInfoRepositoryImpl(
    private val ifConfInfoDataRepository: IfConfInfoDataRepository
) : IfConfInfoRepository {
    override suspend fun save(ifConfInfo: IfConfInfo): IfConfInfo {
        return ifConfInfoDataRepository.save(ifConfInfo)
    }

    override suspend fun deleteById(id: String) {
        ifConfInfoDataRepository.deleteById(id)
    }

    override suspend fun deleteByIfCustInfoId(ifCustInfoId: String) {
        ifConfInfoDataRepository.deleteByIfCustInfoId(ifCustInfoId)
    }
}
