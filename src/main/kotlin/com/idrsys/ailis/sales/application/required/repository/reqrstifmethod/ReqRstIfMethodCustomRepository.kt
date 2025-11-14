package com.idrsys.ailis.sales.application.required.repository.reqrstifmethod

import com.idrsys.ailis.sales.application.dto.query.ReqRstIfMethodQuery
import com.idrsys.ailis.sales.application.dto.request.reqrstifmethod.ReqRstIfMethodSearchParam
import kotlinx.coroutines.flow.Flow

interface ReqRstIfMethodCustomRepository {
    fun findAllByCustMstId(searchParam: ReqRstIfMethodSearchParam): Flow<ReqRstIfMethodQuery>
}
