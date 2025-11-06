package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoResponse
import com.idrsys.ailis.sales.application.usecase.gcgnSalsPicInfo.GcgnSalsPicInfoUseCase
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
@RequestMapping("/api/custs/sals-pics")
@Tag(name = "GcgnSalsPicInfoController", description = "고객 영업담당자 Controller")
class GcgnSalsPicInfoController(
    private val gcgnSalsPicInfoUseCase: GcgnSalsPicInfoUseCase,
) {

    @GetMapping
    @Operation(summary = "getGcgnSalsPicInfoList", description = "고객 영업담당자 정보 목록")
    suspend fun getGcgnSalsPicInfoList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: GcgnSalsPicInfoSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<GcgnSalsPicInfoResponse> {
        return gcgnSalsPicInfoUseCase.getGcgnSalsPicInfoPage(searchParam.copy(custMstId = custMstId), pageable)
    }

    @GetMapping("/{gcgnSalsPicInfoId}")
    @Operation(summary = "getGcgnSalsPicInfoDetail", description = "고객 영업담당자 정보 상세 조회")
    suspend fun getGcgnSalsPicInfoDetail(
        @RequestParam custMstId: String,
        @PathVariable gcgnSalsPicInfoId: Long,
    ): GcgnSalsPicInfoResponse {
        return gcgnSalsPicInfoUseCase.getGcgnSalsPicInfoDetail(custMstId, gcgnSalsPicInfoId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createGcgnSalsPicInfo", description = "고객 영업담당자 정보 등록")
    suspend fun createGcgnSalsPicInfo(
        @RequestParam custMstId: String,
        @RequestBody command: GcgnSalsPicInfoCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): GcgnSalsPicInfoResponse {
        val updatedCommand = command.copy(custMstId = custMstId)
        return gcgnSalsPicInfoUseCase.createGcgnSalsPicInfo(updatedCommand, auth.adminId)
    }

    @PutMapping("/{gcgnSalsPicInfoId}")
    @Operation(summary = "updateGcgnSalsPicInfo", description = "고객 영업담당자 정보 수정")
    suspend fun updateGcgnSalsPicInfo(
        @RequestParam custMstId: String,
        @PathVariable gcgnSalsPicInfoId: Long,
        @RequestBody command: GcgnSalsPicInfoCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): GcgnSalsPicInfoResponse {
        val updatedCommand = command.copy(custMstId = custMstId)
        return gcgnSalsPicInfoUseCase.updateGcgnSalsPicInfo(gcgnSalsPicInfoId, updatedCommand, auth.adminId)
    }
}
