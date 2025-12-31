package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.ifFieldInfo.IfFieldInfoAutoCompleteSearchParam
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoAutoCompleteResponse
import com.idrsys.ailis.sales.application.dto.response.IfFieldInfoResponse
import com.idrsys.ailis.sales.application.usecase.ifFieldInfo.IfFieldInfoUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/excel-fields")
@Tag(name = "IfFieldInfoController", description = "연동 필드 정보 Controller")
class IfFieldInfoController(
    private val ifFieldInfoUseCase: IfFieldInfoUseCase,
) {

    @GetMapping
    @Operation(summary = "getAllIfFieldInfoList", description = "연동 필드 전체 목록 조회 (드롭다운용)")
    suspend fun getAllIfFieldInfoList(): List<IfFieldInfoResponse> {
        return ifFieldInfoUseCase.getAllIfFieldInfoList()
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "getIfFieldInfoAutoCompleteList", description = "연동 필드 자동완성 조회")
    fun getIfFieldInfoAutoCompleteList(
        @ParameterObject searchParam: IfFieldInfoAutoCompleteSearchParam
    ): Flow<IfFieldInfoAutoCompleteResponse> {
        return ifFieldInfoUseCase.getIfFieldInfoAutoCompleteList(searchParam)
    }
}
