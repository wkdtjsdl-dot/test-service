package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.WorklistItemResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import kotlinx.coroutines.flow.Flow

interface WorklistItemUseCase {
    suspend fun search(param: WorklistItemSearchParam): Flow<WorklistItemResponse>
    suspend fun searchForExcel(param: WorklistItemSearchParam): List<WorklistItemResponse>
}
