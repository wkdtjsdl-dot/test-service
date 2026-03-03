package com.idrsys.ailis.sales.application.usecase.hploginuser

import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserCommand
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.HpLoginUserResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HpLoginUserUseCase {
    suspend fun getHpLoginUserPage(searchParam: HpLoginUserSearchParam, pageable: Pageable): Page<HpLoginUserResponse>
    fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserResponse>
    suspend fun createHpLoginUser(custMstId: String, command: HpLoginUserCommand, creator: String): HpLoginUserResponse
    suspend fun updateHpLoginUser(hpLoginUserId: String, command: HpLoginUserUpdateCommand, updater: String): HpLoginUserResponse
    suspend fun deleteHpLoginUser(hpLoginUserId: String)
}
