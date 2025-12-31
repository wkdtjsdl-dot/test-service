package com.idrsys.ailis.sales.application.usecase.ifFieldInfo

import com.idrsys.ailis.sales.application.dto.request.ifFieldInfo.IfFieldInfoAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import kotlinx.coroutines.flow.Flow

interface IfFieldInfoUseCase {
    suspend fun getAllIfFieldInfoList(): List<IfFieldInfoResponse>
    fun getIfFieldInfoAutoCompleteList(searchParam: IfFieldInfoAutoCompleteSearchParam): Flow<IfFieldInfoAutoCompleteResponse>
}
