package com.idrsys.ailis.tst.application.usecase

import com.idrsys.ailis.tst.application.dto.WorkListAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemUpdateRequest
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.application.dto.WorkListUpdateRequest
import com.idrsys.ailis.tst.application.dto.request.WorkListAutoCompleteSearchParam
import com.idrsys.ailis.tst.application.dto.request.WorkListSearchParam
import kotlinx.coroutines.flow.Flow

interface WorkListUseCase {
    fun getWorkLists(searchParam: WorkListSearchParam): Flow<WorkListResponse>
    fun getWorkListItems(wrklistCd: String): Flow<WorkListItemDetailResponse>
    fun getWorkListItemsByCds(wrklistCds: List<String>): Flow<WorkListItemDetailResponse>
    fun autoCompleteWorkLists(searchParam: WorkListAutoCompleteSearchParam): Flow<WorkListAutoCompleteResponse>
    fun getSimpleList(useYn: Boolean? = null): Flow<WorkListAutoCompleteResponse>
    suspend fun registerWorkList(request: WorkListRegisterRequest, adminId: String): WorkListResponse
    suspend fun updateWorkList(wrklistCd: String, request: WorkListUpdateRequest, adminId: String): WorkListResponse
    suspend fun registerWorkListItem(request: WorkListItemRegisterRequest, adminId: String): WorkListItemResponse
    suspend fun updateWorkListItem(
        wrklistItmId: String,
        wrklistCd: String,
        request: WorkListItemUpdateRequest,
        adminId: String
    ): WorkListItemResponse
    suspend fun deleteWorkListItem(wrklistItmId: String, wrklistCd: String, adminId: String)
}
