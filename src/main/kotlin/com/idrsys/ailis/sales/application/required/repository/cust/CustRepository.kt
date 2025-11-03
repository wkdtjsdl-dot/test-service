package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.domain.model.Cust

interface CustRepository {
    suspend fun save(cust: Cust): Cust
}