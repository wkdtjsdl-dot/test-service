package com.idrsys.ailis.sales.application.required.repository.apprinfo

import com.idrsys.ailis.sales.domain.model.ApprInfo

/**
 * 결재정보 CUD Repository (Port)
 */
interface ApprInfoRepository {
    suspend fun save(apprInfo: ApprInfo): ApprInfo
    suspend fun findById(id: String): ApprInfo?
    suspend fun deleteById(id: String)
    suspend fun findAllByApprInfoNo(apprInfoNo: Long): List<ApprInfo>
    suspend fun deleteAllByApprInfoNo(apprInfoNo: Long)
}
