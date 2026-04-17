package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.WorklistItemResponse
import com.idrsys.ailis.tst.application.dto.WorklistItemSearchParam
import kotlinx.coroutines.flow.Flow

interface WorklistItemRepository {
    fun search(param: WorklistItemSearchParam): Flow<WorklistItemResponse>
    fun searchForExcel(param: WorklistItemSearchParam): Flow<WorklistItemResponse>
}
