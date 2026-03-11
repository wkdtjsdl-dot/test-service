package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.domain.model.Cust

interface CustRepository {
    suspend fun findByCustMstId(custMstId: String): Cust?
    suspend fun save(cust: Cust): Cust
}