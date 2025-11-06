package com.idrsys.ailis.sales.application.usecase.salsAction

import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SalsActionUseCase {
    suspend fun getSalsActionPage(searchParam: SalsActionSearchParam, pageable: Pageable): Page<SalsActionResponse>
    suspend fun getSalsActionDetail(custMstId: String, salsActionId: Long): SalsActionResponse
    suspend fun createSalsAction(custMstId: String, command: SalsActionCommand, adminId: String): SalsActionResponse
    suspend fun updateSalsAction(custMstId: String, salsActionId: Long, command: SalsActionCommand, adminId: String): SalsActionResponse
}
