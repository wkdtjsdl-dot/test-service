package com.idrsys.ailis.tst.application.required.repository

import com.idrsys.ailis.tst.application.dto.WorkListAutoCompleteResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.request.WorkListAutoCompleteSearchParam
import com.idrsys.ailis.tst.application.dto.request.WorkListSearchParam
import com.idrsys.ailis.tst.domain.model.WorkList
import com.idrsys.ailis.tst.domain.model.WorkListItem
import kotlinx.coroutines.flow.Flow

interface WorkListRepository {
    suspend fun save(workList: WorkList): WorkList
    suspend fun findById(wrklistCd: String): WorkList?
    fun findBySearch(searchParam: WorkListSearchParam): Flow<WorkList>
    fun autoCompleteWorkLists(searchParam: WorkListAutoCompleteSearchParam): Flow<WorkListAutoCompleteResponse>
    fun getSimpleList(useYn: Boolean? = null): Flow<WorkListAutoCompleteResponse>

    suspend fun saveItem(workListItem: WorkListItem): WorkListItem
    suspend fun findItemByIdAndWrklistCd(wrklistItmId: String, wrklistCd: String): WorkListItem?
    suspend fun findItemByWrklistCdAndTstCd(wrklistCd: String, tstCd: String): WorkListItem?
    suspend fun deleteItemById(wrklistItmId: String)
    fun findItemsByWrklistCd(wrklistCd: String): Flow<WorkListItemDetailResponse>
    fun findItemsByWrklistCds(wrklistCds: List<String>): Flow<WorkListItemDetailResponse>
}
