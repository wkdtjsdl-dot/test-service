package com.idrsys.ailis.sales.application.usecase.salsAction

import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SalsActionUseCase {
    suspend fun getSalsActionPage(searchParam: SalsActionSearchParam, pageable: Pageable): Page<SalsActionResponse>
    suspend fun getSalsActionDetail(salsActionId: Long): SalsActionResponse
    suspend fun createSalsAction(command: SalsActionCommand, adminId: String): SalsActionResponse
    suspend fun updateSalsAction(salsActionId: Long, command: SalsActionCommand, adminId: String): SalsActionResponse
    suspend fun deleteSalsAction(salsActionId: Long)
}
