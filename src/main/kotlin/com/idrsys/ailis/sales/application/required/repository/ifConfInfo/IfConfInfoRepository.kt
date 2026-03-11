package com.idrsys.ailis.sales.application.required.repository.ifConfInfo

import com.idrsys.ailis.sales.domain.model.IfConfInfo

interface IfConfInfoRepository {
    suspend fun save(ifConfInfo: IfConfInfo): IfConfInfo
    suspend fun deleteById(id: String)
    suspend fun deleteByIfCustInfoId(ifCustInfoId: String)
}
