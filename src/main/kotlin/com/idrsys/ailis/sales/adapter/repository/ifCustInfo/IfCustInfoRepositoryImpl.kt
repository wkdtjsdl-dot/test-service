package com.idrsys.ailis.sales.adapter.repository.ifCustInfo

import com.idrsys.ailis.sales.application.required.repository.ifCustInfo.IfCustInfoRepository
import com.idrsys.ailis.sales.domain.model.IfCustInfo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IfCustInfoDataRepository : CoroutineCrudRepository<IfCustInfo, String> {
    suspend fun deleteByCustMstId(custMstId: String)
}

@Repository
class IfCustInfoRepositoryImpl(
    private val ifCustInfoDataRepository: IfCustInfoDataRepository
) : IfCustInfoRepository {
    override suspend fun save(ifCustInfo: IfCustInfo): IfCustInfo {
        return ifCustInfoDataRepository.save(ifCustInfo)
    }

    override suspend fun findById(id: String): IfCustInfo? {
        return ifCustInfoDataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        ifCustInfoDataRepository.deleteById(id)
    }

    override suspend fun deleteByCustMstId(custMstId: String) {
        ifCustInfoDataRepository.deleteByCustMstId(custMstId)
    }
}
