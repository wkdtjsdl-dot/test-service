package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.contract.ContractCommand
import com.idrsys.ailis.sales.application.dto.request.contract.ContractSearchParam
import com.idrsys.ailis.sales.application.dto.response.ContractListResponse
import com.idrsys.ailis.sales.application.dto.response.ContractResponse
import com.idrsys.ailis.sales.application.usecase.contract.ContractUseCase
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
@RequestMapping("/api/custs/{custMstId}/cntrs")
@Tag(name = "ContractController", description = "고객 계약 Controller")
class ContractController(
    private val contractUseCase: ContractUseCase,
) {

    @GetMapping
    @Operation(summary = "getContractList", description = "고객 계약정보 목록")
    suspend fun getContractList(
        @PathVariable custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: ContractSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<ContractListResponse> {
        val updatedSearchParam = searchParam.copy(custMstId = custMstId)
        return contractUseCase.getContractPage(updatedSearchParam, pageable)
    }

    @GetMapping("/{custCntrId}")
    @Operation(summary = "getContractDetail", description = "고객 계약정보 상세 조회")
    suspend fun getContractDetail(
        @PathVariable custMstId: String,
        @PathVariable custCntrId: Long,
    ): ContractResponse {
        return contractUseCase.getContractDetail(custMstId, custCntrId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createContract", description = "고객 계약정보 등록")
    suspend fun createContract(
        @PathVariable custMstId: String,
        @RequestBody command: ContractCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): ContractResponse {
        return contractUseCase.createContract(custMstId, command, auth.adminId)
    }

    @PutMapping("/{custCntrId}")
    @Operation(summary = "updateContract", description = "고객 계약정보 수정")
    suspend fun updateContract(
        @PathVariable custMstId: String,
        @PathVariable custCntrId: Long,
        @RequestBody command: ContractCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): ContractResponse {
        return contractUseCase.updateContract(custMstId, custCntrId, command, auth.adminId)
    }
}
