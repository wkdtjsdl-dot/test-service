package com.idrsys.ailis.sales.application.required.repository.ifCustInfo

import com.idrsys.ailis.sales.domain.model.IfCustInfo

interface IfCustInfoRepository {
    suspend fun save(ifCustInfo: IfCustInfo): IfCustInfo
    suspend fun findById(id: String): IfCustInfo?
    suspend fun deleteById(id: String)
}
