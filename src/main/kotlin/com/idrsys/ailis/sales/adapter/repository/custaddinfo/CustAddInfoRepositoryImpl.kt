package com.idrsys.ailis.sales.adapter.repository.custaddinfo

import com.idrsys.ailis.sales.application.required.repository.custaddinfo.CustAddInfoRepository
import com.idrsys.ailis.sales.domain.model.CustAddInfo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustAddInfoDataRepository : CoroutineCrudRepository<CustAddInfo, Long>

@Repository
class CustAddInfoRepositoryImpl(
    private val dataRepository: CustAddInfoDataRepository
) : CustAddInfoRepository {

    override suspend fun findById(id: Long): CustAddInfo? {
        return dataRepository.findById(id)
    }

    override suspend fun save(custAddInfo: CustAddInfo): CustAddInfo {
        return dataRepository.save(custAddInfo)
    }
}
