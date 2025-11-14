package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custaddinfo.CustAddInfoCommand
import com.idrsys.ailis.sales.application.dto.response.CustAddInfoResponse
import com.idrsys.ailis.sales.application.usecase.custaddinfo.CustAddInfoUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/add-info")
@Tag(name = "CustAddInfoController", description = "고객 추가정보(특이사항) Controller")
class CustAddInfoController(
    private val custAddInfoUseCase: CustAddInfoUseCase
) {

    @GetMapping("/{custAddInfoId}")
    @Operation(summary = "findCustAddInfoById", description = "고객 추가정보(특이사항) 상세 조회")
    suspend fun findCustAddInfoById(
        @PathVariable custAddInfoId: Long
    ): CustAddInfoResponse? {
        return custAddInfoUseCase.findCustAddInfoById(custAddInfoId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createCustAddInfo", description = "고객 추가정보(특이사항) 등록")
    suspend fun createCustAddInfo(
        @RequestBody command: CustAddInfoCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustAddInfoResponse {
        return custAddInfoUseCase.createCustAddInfo(command, auth.adminId)
    }

    @PutMapping("/{custAddInfoId}")
    @Operation(summary = "updateCustAddInfo", description = "고객 추가정보(특이사항) 수정")
    suspend fun updateCustAddInfo(
        @PathVariable custAddInfoId: Long,
        @RequestBody command: CustAddInfoCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustAddInfoResponse {
        return custAddInfoUseCase.updateCustAddInfo(custAddInfoId, command, auth.adminId)
    }
}
