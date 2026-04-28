package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.WorklistItemStatResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import kotlinx.coroutines.flow.Flow

interface WorklistItemRepository {
    fun search(param: WorklistItemSearchParam): Flow<WorklistItemStatResponse>
    fun searchForExcel(param: WorklistItemSearchParam): Flow<WorklistItemStatResponse>
}
