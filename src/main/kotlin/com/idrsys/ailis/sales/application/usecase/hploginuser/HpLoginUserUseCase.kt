package com.idrsys.ailis.sales.application.usecase.hploginuser

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import kotlinx.coroutines.flow.Flow

interface HpLoginUserUseCase {
    suspend fun findById(hpLoginUserId: String): HpLoginUserResponse?
    fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserResponse>
    suspend fun save(command: HpLoginUserCommand, creator: String): HpLoginUserResponse
}
