package com.idrsys.ailis.sales.application.required.repository.salsAction

import com.idrsys.ailis.sales.application.dto.query.SalsActionQuery
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.domain.model.SalsAction
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface SalsActionCustomRepository {
    fun findSalsActions(searchParam: SalsActionSearchParam, pageable: Pageable?): Flow<SalsActionQuery>
    suspend fun countSalsActions(searchParam: SalsActionSearchParam): Long
    suspend fun findSalsActionById(salsActionId: Long): SalsActionQuery?
    suspend fun findDomainById(id: Long): SalsAction?
}
