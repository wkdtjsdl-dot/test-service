package com.idrsys.ailis.sales.application.required.repository.cust

import com.idrsys.ailis.sales.domain.model.CustMstHst

interface CustMstHstRepository {
    suspend fun save(custMstHst: CustMstHst): CustMstHst
}

