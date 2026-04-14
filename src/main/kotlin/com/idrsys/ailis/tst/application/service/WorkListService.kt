package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemUpdateRequest
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.application.dto.WorkListUpdateRequest
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
            throw IllegalStateException("${request.wrklistCd}: 이미 존재하는 워크코드입니다.")
        }

        val command = commandMapper.toCreateCommand(request)
        val now = LocalDateTime.now()
        val domain = WorkList.create(command, adminId, now)
        val saved = repository.save(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun updateWorkList(
        wrklistCd: String,
        request: WorkListUpdateRequest,
        adminId: String
    ): WorkListResponse {
        val workList = repository.findById(wrklistCd)
            ?: throw RuntimeException("$wrklistCd: 워크를 찾을 수 없습니다.")

        val command = commandMapper.toUpdateCommand(request)
        val now = LocalDateTime.now()
        val saved = repository.save(workList.update(command, adminId, now))
        return mapper.toResponse(saved)
    }

    override suspend fun registerWorkListItem(request: WorkListItemRegisterRequest, adminId: String): WorkListItemResponse {
        val workList = repository.findById(request.wrklistCd)
            ?: throw RuntimeException("${request.wrklistCd}: 워크를 찾을 수 없습니다.")

        val duplicated = repository.findItemByWrklistCdAndTstCd(workList.wrklistCd, request.tstCd)
        if (duplicated != null) {
            throw IllegalStateException("해당 검사항목은 워크 하위에 이미 존재하는 검사항목입니다.")
        }

        val command = commandMapper.toCreateItemCommand(request)
        val now = LocalDateTime.now()
        val domain = WorkListItem.create(command, adminId, now)
        val saved = repository.saveItem(domain)
        return mapper.toResponse(saved)
    }

    override suspend fun updateWorkListItem(
        wrklistItmId: String,
        wrklistCd: String,
        request: WorkListItemUpdateRequest,
        adminId: String
    ): WorkListItemResponse {
        val workListItem = repository.findItemByIdAndWrklistCd(wrklistItmId, wrklistCd)
            ?: throw RuntimeException("해당 검사항목 정보를 찾을 수 없습니다.")

        val duplicated = repository.findItemByWrklistCdAndTstCd(wrklistCd, request.tstCd)
        if (duplicated != null && duplicated.wrklistItmId != wrklistItmId) {
            throw IllegalStateException("해당 검사항목은 워크 하위에 이미 존재하는 검사항목입니다.")
        }

        val command = commandMapper.toUpdateItemCommand(request)
        val now = LocalDateTime.now()
        val saved = repository.saveItem(workListItem.update(command, adminId, now))
        return mapper.toResponse(saved)
    }

    override suspend fun deleteWorkListItem(wrklistItmId: String, wrklistCd: String, adminId: String) {
        val workListItem = repository.findItemByIdAndWrklistCd(wrklistItmId, wrklistCd)
            ?: throw RuntimeException("해당 검사항목 정보를 찾을 수 없습니다.")

        repository.deleteItemById(workListItem.wrklistItmId!!)
    }
}
