package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import com.idrsys.ailis.sales.application.usecase.custaddinfo.CustAddInfoUseCase
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
@RequestMapping("/api/custs/add-info")
@Tag(name = "CustAddInfoController", description = "고객 추가정보(특이사항) Controller")
class CustAddInfoController(
    private val custAddInfoUseCase: CustAddInfoUseCase
) {

    @GetMapping
    @Operation(summary = "getCustAddInfoList", description = "고객 추가정보(특이사항) 목록")
    suspend fun getCustAddInfoList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: CustAddInfoSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<CustAddInfoResponse> {
        val updatedSearchParam = searchParam.copy(custMstId = custMstId)
        return custAddInfoUseCase.getCustAddInfoPage(updatedSearchParam, pageable)
    }

    @GetMapping("/{custMstId}")
    @Operation(summary = "getCustAddInfoDetail", description = "고객 추가정보(특이사항) 상세 조회")
    suspend fun getCustAddInfoDetail(
        @PathVariable custMstId: String
    ): CustAddInfoResponse {
        return custAddInfoUseCase.getCustAddInfoDetailByCustMstId(custMstId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createCustAddInfo", description = "고객 추가정보(특이사항) 등록")
    suspend fun createCustAddInfo(
        @RequestBody command: CustAddInfoCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustAddInfoResponse {
        return custAddInfoUseCase.createCustAddInfo(command.custMstId, command, auth.adminId)
    }

    @PutMapping("/{custAddInfoId}")
    @Operation(summary = "updateCustAddInfo", description = "고객 추가정보(특이사항) 수정")
    suspend fun updateCustAddInfo(
        @PathVariable custAddInfoId: Long,
        @RequestBody command: CustAddInfoCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustAddInfoResponse {
        return custAddInfoUseCase.updateCustAddInfo(command.custMstId, custAddInfoId, command, auth.adminId)
    }

    @DeleteMapping("/{custAddInfoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteCustAddInfo", description = "고객 추가정보(특이사항) 삭제")
    suspend fun deleteCustAddInfo(
        @PathVariable custAddInfoId: Long
    ) {
        custAddInfoUseCase.deleteCustAddInfo(custAddInfoId)
    }
}
