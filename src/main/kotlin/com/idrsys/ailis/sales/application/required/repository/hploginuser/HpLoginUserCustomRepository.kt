package com.idrsys.ailis.sales.application.required.repository.hploginuser

import com.idrsys.ailis.sales.application.dto.query.HpLoginUserQuery
import com.idrsys.ailis.sales.application.dto.request.hploginuser.HpLoginUserSearchParam
import com.idrsys.ailis.sales.domain.model.HpLoginUser
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface HpLoginUserCustomRepository {
    fun findAllByCustMstId(searchParam: HpLoginUserSearchParam): Flow<HpLoginUserQuery>
    fun findHpLoginUsers(searchParam: HpLoginUserSearchParam, pageable: Pageable?): Flow<HpLoginUserQuery>
    suspend fun countHpLoginUsers(searchParam: HpLoginUserSearchParam): Long
    suspend fun findDomainById(id: String): HpLoginUser?
}
