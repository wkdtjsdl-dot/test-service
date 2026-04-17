package com.idrsys.ailis.tst.application.service

import com.idrsys.ailis.tst.application.dto.WorklistItemStatResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import com.idrsys.ailis.tst.application.required.repository.WorklistItemRepository
import com.idrsys.ailis.tst.application.usecase.WorklistItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorklistItemService(
    private val worklistItemRepository: WorklistItemRepository
) : WorklistItemUseCase {

    @Transactional(readOnly = true)
    override suspend fun search(param: WorklistItemSearchParam): Flow<WorklistItemStatResponse> {
        return worklistItemRepository.search(param)
    }

    @Transactional(readOnly = true)
    override suspend fun searchForExcel(param: WorklistItemSearchParam): List<WorklistItemStatResponse> {
        return worklistItemRepository.searchForExcel(param).toList()
    }
}
