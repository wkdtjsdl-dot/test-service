package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactCommand
import com.idrsys.ailis.sales.application.dto.request.custContact.CustContactSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustContactResponse
import com.idrsys.ailis.sales.application.usecase.custContact.CustContactUseCase
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
@RequestMapping("/api/custs/contacts")
@Tag(name = "CustContactController", description = "고객 연락처 Controller")
class CustContactController(
    private val custContactUseCase: CustContactUseCase,
) {

    @GetMapping
    @Operation(summary = "getCustContactList", description = "고객 연락처 정보 목록")
    suspend fun getCustContactList(
        @RequestParam custMstId: String,
        @ParameterObject @Parameter(hidden = true) searchParam: CustContactSearchParam,
        @PageableDefault(page = 0, size = 15) pageable: Pageable,
    ): Page<CustContactResponse> {
        return custContactUseCase.getCustContactPage(searchParam.copy(custMstId = custMstId), pageable)
    }

    @GetMapping("/{custContactId}")
    @Operation(summary = "getCustContactDetail", description = "고객 연락처 정보 상세 조회")
    suspend fun getCustContactDetail(
        @PathVariable custContactId: Long,
    ): CustContactResponse {
        return custContactUseCase.getCustContactDetail(custContactId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "createCustContact", description = "고객 연락처 정보 등록")
    suspend fun createCustContact(
        @RequestBody command: CustContactCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): CustContactResponse {
        return custContactUseCase.createCustContact(command, auth.adminId)
    }

    @PutMapping("/{custContactId}")
    @Operation(summary = "updateCustContact", description = "고객 연락처 정보 수정")
    suspend fun updateCustContact(
        @PathVariable custContactId: Long,
        @RequestBody command: CustContactCommand,
        @JwtAuthorization auth: AuthenticationAdmin,
    ): CustContactResponse {
        return custContactUseCase.updateCustContact(custContactId, command, auth.adminId)
    }
}
