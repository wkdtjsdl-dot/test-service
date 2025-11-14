package com.idrsys.ailis.sales.application.required.repository.custaddinfo

import com.idrsys.ailis.sales.application.dto.query.CustAddInfoQuery
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import kotlinx.coroutines.flow.Flow

interface CustAddInfoCustomRepository {
    fun findAllByCustMstId(searchParam: CustAddInfoSearchParam): Flow<CustAddInfoQuery>
}
