package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.WorkListItemDetailResponse
import com.idrsys.ailis.tst.application.dto.WorkListItemRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListItemResponse
import com.idrsys.ailis.tst.application.dto.WorkListRegisterRequest
import com.idrsys.ailis.tst.application.dto.WorkListResponse
import com.idrsys.ailis.tst.application.dto.request.WorkListSearchParam
import com.idrsys.ailis.tst.application.usecase.WorkListUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Work List", description = "워크리스트 관리 API")
@RestController
@RequestMapping("/api/bbs/wrklist")
class WorkListController(
    private val workListUseCase: WorkListUseCase
) {

    @Operation(summary = "워크리스트 조회")
    @GetMapping
    fun getWorkLists(@ParameterObject searchParam: WorkListSearchParam): Flow<WorkListResponse> {
        return workListUseCase.getWorkLists(searchParam)
    }

    @Operation(summary = "워크리스트 하위 검사항목 리스트 조회")
    @GetMapping("/{wrklistCd}/items")
    fun getWorkListItems(@PathVariable wrklistCd: String): Flow<WorkListItemDetailResponse> {
        return workListUseCase.getWorkListItems(wrklistCd)
    }

    @Operation(summary = "워크리스트 생성")
    @PostMapping
    suspend fun registerWorkList(
        @RequestBody request: WorkListRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): WorkListResponse {
        return workListUseCase.registerWorkList(request, auth.adminId)
    }

    @Operation(summary = "워크리스트 하위 검사항목 추가")
    @PostMapping("/item")
    suspend fun registerWorkListItem(
        @RequestBody request: WorkListItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): WorkListItemResponse {
        return workListUseCase.registerWorkListItem(request, auth.adminId)
    }

    @Operation(summary = "워크리스트 하위 검사항목 삭제")
    @DeleteMapping("/item/{wrklistItmId}")
    suspend fun deleteWorkListItem(
        @PathVariable wrklistItmId: String,
        @RequestParam wrklistCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ) {
        workListUseCase.deleteWorkListItem(wrklistItmId, wrklistCd, auth.adminId)
    }
}
