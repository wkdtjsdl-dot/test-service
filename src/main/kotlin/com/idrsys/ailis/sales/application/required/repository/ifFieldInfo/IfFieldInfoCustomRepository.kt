package com.idrsys.ailis.sales.application.required.repository.ifFieldInfo

import com.idrsys.ailis.sales.application.dto.query.IfFieldInfoQuery
import com.idrsys.ailis.sales.application.dto.request.ifFieldInfo.IfFieldInfoAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import kotlinx.coroutines.flow.Flow

interface IfFieldInfoCustomRepository {
    fun findAllIfFieldInfos(): Flow<IfFieldInfoQuery>
    fun findIfFieldInfoAutoCompleteList(searchParam: IfFieldInfoAutoCompleteSearchParam): Flow<IfFieldInfoAutoCompleteResponse>
}
