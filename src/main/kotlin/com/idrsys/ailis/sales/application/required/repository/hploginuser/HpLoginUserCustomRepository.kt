package com.idrsys.ailis.sales.application.required.repository.hploginuser

import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import kotlinx.coroutines.flow.Flow

interface HpLoginUserCustomRepository {
    fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserQuery>
}
