package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.application.dto.request.WorkListSearchParam
import com.idrsys.ailis.tst.application.mapper.WorkListCommandMapper
import com.idrsys.ailis.tst.application.mapper.WorkListMapper
import com.idrsys.ailis.tst.application.required.repository.WorkListRepository
import com.idrsys.ailis.tst.application.usecase.WorkListUseCase
import com.idrsys.ailis.tst.domain.model.WorkList
import com.idrsys.ailis.tst.domain.model.WorkListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class WorkListService(
    private val repository: WorkListRepository,
    private val mapper: WorkListMapper,
    private val commandMapper: WorkListCommandMapper
) : WorkListUseCase {

    @Transactional(readOnly = true)
    override fun getWorkLists(searchParam: WorkListSearchParam): Flow<WorkListResponse> {
        return repository.findBySearch(searchParam).map { mapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getWorkListItems(wrklistCd: String): Flow<WorkListItemDetailResponse> {
        return repository.findItemsByWrklistCd(wrklistCd)
    }

    override suspend fun registerWorkList(request: WorkListRegisterRequest, adminId: String): WorkListResponse {
        if (repository.findById(request.wrklistCd) != null) {
            throw IllegalStateException("Work list already exists: ${request.wrklistCd}")
        }

        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = WorkList.create(command, adminId, now)
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun registerWorkListItem(request: WorkListItemRegisterRequest, adminId: String): WorkListItemResponse {
        val workList = repository.findById(request.wrklistCd)
            ?: throw RuntimeException("Work list not found with code: ${request.wrklistCd}")

        val duplicated = repository.findItemByWrklistCdAndTstCd(workList.wrklistCd, request.tstCd)
        if (duplicated != null) {
            throw IllegalStateException("Work list item already exists for wrklistCd=${request.wrklistCd}, tstCd=${request.tstCd}")
        }

        val command = commandMapper.toCreateItemCommand(request)
        val now = LocalDateTime.now()
        val domain = WorkListItem.create(command, adminId, now)
        val saved = repository.saveItem(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun deleteWorkListItem(wrklistItmId: String, wrklistCd: String, adminId: String) {
        val workListItem = repository.findItemByIdAndWrklistCd(wrklistItmId, wrklistCd)
            ?: throw RuntimeException("Work list item not found with id: $wrklistItmId and wrklistCd: $wrklistCd")

        repository.deleteItemById(workListItem.wrklistItmId!!)
    }
}
