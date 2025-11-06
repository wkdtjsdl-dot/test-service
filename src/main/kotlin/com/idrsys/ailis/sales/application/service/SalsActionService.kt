package com.idrsys.ailis.sales.application.service

import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionCommand
import com.idrsys.ailis.sales.application.dto.request.salsAction.SalsActionSearchParam
import com.idrsys.ailis.sales.application.dto.response.SalsActionResponse
import com.idrsys.ailis.sales.application.required.repository.salsAction.SalsActionCustomRepository
import com.idrsys.ailis.sales.application.required.repository.salsAction.SalsActionRepository
import com.idrsys.ailis.sales.application.usecase.salsAction.SalsActionUseCase
import com.idrsys.ailis.sales.domain.model.SalsAction
import com.idrsys.ailis.sales.shared.mapper.SalsActionMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SalsActionService(
    private val salsActionRepository: SalsActionRepository,
    private val salsActionCustomRepository: SalsActionCustomRepository,
    private val salsActionMapper: SalsActionMapper,
) : SalsActionUseCase {

    override suspend fun getSalsActionPage(searchParam: SalsActionSearchParam, pageable: Pageable): Page<SalsActionResponse> {
        val total = salsActionCustomRepository.countSalsActions(searchParam)
        if (total == 0L) return PageImpl(emptyList(), pageable, 0)

        val salsActions = salsActionCustomRepository.findSalsActions(searchParam, pageable).map {
            salsActionMapper.toResponse(it)
        }.toList()

        return PageImpl(salsActions, pageable, total)
    }

    override suspend fun getSalsActionDetail(custMstId: String, salsActionId: Long): SalsActionResponse {
        val dto = salsActionCustomRepository.findSalsActionById(salsActionId)
            ?: throw NoSuchElementException("SalsAction not found with id: $salsActionId")
        return salsActionMapper.toResponse(dto)
    }

    override suspend fun createSalsAction(custMstId: String, command: SalsActionCommand, adminId: String): SalsActionResponse {
        val now = LocalDateTime.now()
        val salsAction = SalsAction(
            custMstId = custMstId,
            custCd = command.custCd,
            visitDtime = command.visitDtime,
            visitPrpsCd = command.visitPrpsCd,
            visitTargetPersonNm = command.visitTargetPersonNm,
            visitTargetPersonContact = command.visitTargetPersonContact,
            memo = command.memo,
            useYn = command.useYn,
            creator = adminId,
            createDtime = now,
            updater = adminId,
            updateDtime = now
        ).apply { setAsNew() }

        val savedSalsAction = salsActionRepository.save(salsAction)
        return salsActionMapper.toResponse(savedSalsAction)
    }

    override suspend fun updateSalsAction(custMstId: String, salsActionId: Long, command: SalsActionCommand, adminId: String): SalsActionResponse {
        val salsAction = salsActionCustomRepository.findDomainById(salsActionId)
            ?: throw NoSuchElementException("SalsAction not found with id: $salsActionId")

        salsAction.update(command, adminId)

        val updatedSalsAction = salsActionRepository.save(salsAction)
        return salsActionMapper.toResponse(updatedSalsAction)
    }
}
