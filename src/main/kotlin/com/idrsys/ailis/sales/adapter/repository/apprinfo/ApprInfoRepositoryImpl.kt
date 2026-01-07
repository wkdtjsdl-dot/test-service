package com.idrsys.ailis.sales.adapter.repository.apprinfo

import com.idrsys.ailis.sales.application.required.repository.apprinfo.ApprInfoRepository
import com.idrsys.ailis.sales.domain.model.ApprInfo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data R2DBC용 내부 인터페이스
 */
@Repository
interface ApprInfoDataRepository : CoroutineCrudRepository<ApprInfo, String> {
    suspend fun findAllByApprInfoNo(apprInfoNo: Long): List<ApprInfo>
}

/**
 * 결재정보 CUD Repository 구현체 (Adapter)
 */
@Repository
class ApprInfoRepositoryImpl(
    private val dataRepository: ApprInfoDataRepository
) : ApprInfoRepository {

    override suspend fun save(apprInfo: ApprInfo): ApprInfo {
        return dataRepository.save(apprInfo)
    }

    override suspend fun findById(id: String): ApprInfo? {
        return dataRepository.findById(id)
    }

    override suspend fun deleteById(id: String) {
        dataRepository.deleteById(id)
    }

    override suspend fun findAllByApprInfoNo(apprInfoNo: Long): List<ApprInfo> {
        return dataRepository.findAllByApprInfoNo(apprInfoNo)
    }
}
