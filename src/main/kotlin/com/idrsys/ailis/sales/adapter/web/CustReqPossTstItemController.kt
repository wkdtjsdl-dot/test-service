package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemCommand
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemSearchParam
import com.idrsys.ailis.sales.application.dto.request.custreqposststitem.CustReqPossTstItemUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.CustReqPossTstItemResponse
import com.idrsys.ailis.sales.application.usecase.custreqposststitem.CustReqPossTstItemUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/req-tst-item")
@Tag(name = "CustReqPossTstItemController", description = "고객 추가정보(의뢰가능검사) Controller")
class CustReqPossTstItemController(
    private val useCase: CustReqPossTstItemUseCase
) {

    @GetMapping
    @Operation(summary = "getCustReqPossTstItemList", description = "의뢰가능검사 목록 조회")
    suspend fun getCustReqPossTstItemList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: CustReqPossTstItemSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable
    ): Page<CustReqPossTstItemResponse> {
        val updatedSearchParam = searchParam.copy(custMstId = custMstId)
        return useCase.getCustReqPossTstItemPage(updatedSearchParam, pageable)
    }

    @GetMapping("/{custReqPossTstItemId}")
    @Operation(summary = "findItemById", description = "의뢰가능검사 단건 조회")
    suspend fun findItemById(
        @PathVariable custReqPossTstItemId: Long
    ): CustReqPossTstItemResponse? {
        return useCase.findItemById(custReqPossTstItemId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "saveItem", description = "의뢰가능검사 저장")
    suspend fun saveItem(
        @RequestBody command: CustReqPossTstItemCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustReqPossTstItemResponse {
        return useCase.saveItem(command, auth.adminId)
    }

    @PutMapping("/{custReqPossTstItemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "updateItem", description = "의뢰가능검사 수정")
    suspend fun updateItem(
        @PathVariable custReqPossTstItemId: Long,
        @RequestBody command: CustReqPossTstItemUpdateCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustReqPossTstItemResponse {
        return useCase.updateItem(custReqPossTstItemId, command, auth.adminId)
    }

    @DeleteMapping("/{custReqPossTstItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteCustReqPossTstItem", description = "의뢰가능검사 삭제")
    suspend fun deleteCustReqPossTstItem(
        @PathVariable custReqPossTstItemId: Long
    ) {
        useCase.deleteCustReqPossTstItem(custReqPossTstItemId)
    }
}
