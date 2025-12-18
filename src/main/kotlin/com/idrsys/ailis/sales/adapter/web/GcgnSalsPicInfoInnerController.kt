package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalaPicInfoAutoSearchParam
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoCommand
import com.idrsys.ailis.sales.application.dto.request.gcgnSalsPicInfo.GcgnSalsPicInfoSearchParam
import com.idrsys.ailis.sales.application.dto.response.GcgnSalsPicInfoAutoResponse
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
@RequestMapping("/api/inner/custs/sals-pics")
@Tag(name = "GcgnSalsPicInfoController", description = "고객 영업담당자 Inner Controller")
class GcgnSalsPicInfoInnerController(
    private val gcgnSalsPicInfoUseCase: GcgnSalsPicInfoUseCase,
) {

    @GetMapping
    @Operation(summary = "getGcgnSalsPicInfoList", description = "고객 영업담당자 정보 목록")
    suspend fun getGcgnSalsPicInfoList(
        @ParameterObject @Parameter(hidden = true) searchParam: GcgnSalsPicInfoSearchParam,
    ): Page<GcgnSalsPicInfoResponse> {
        return gcgnSalsPicInfoUseCase.getGcgnSalsPicInfoPage(searchParam, Pageable.unpaged())
    }

    @GetMapping("/autoComplete")
    @Operation(summary = "getSalsPicAutoCompleteList", description = "영업담당자 자동완성 조회")
    suspend fun getSalsPicAutoCompleteList(
        @ParameterObject @Parameter(hidden = true) searchParam: GcgnSalaPicInfoAutoSearchParam
    ) : List<GcgnSalsPicInfoAutoResponse> {
        return gcgnSalsPicInfoUseCase.getSalsPicAutoCompleteList(searchParam)
    }
}
