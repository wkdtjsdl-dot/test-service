package com.idrsys.ailis.sales.application.required.repository.custaddinfo

import com.idrsys.ailis.sales.domain.model.CustAddInfo

interface CustAddInfoRepository {
    suspend fun findById(id: Long): CustAddInfo?
    suspend fun save(custAddInfo: CustAddInfo): CustAddInfo
}
