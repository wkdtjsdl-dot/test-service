package com.idrsys.ailis.sales.application.required.repository.custatchfile

import com.idrsys.ailis.sales.application.dto.query.CustAtchFileQuery
import kotlinx.coroutines.flow.Flow

interface CustAtchFileCustomRepository {
    fun findAllByCustMstId(custMstId: String): Flow<CustAtchFileQuery>
}
