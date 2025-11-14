package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileCommand
import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAtchFileResponse
import com.idrsys.ailis.sales.application.usecase.custatchfile.CustAtchFileUseCase
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/atch-file")
@Tag(name = "CustAtchFileController", description = "고객 추가정보(첨부문서) Controller")
class CustAtchFileController(
    private val custAtchFileUseCase: CustAtchFileUseCase
) {

    @GetMapping("/{custAddInfoId}")
    @Operation(summary = "getFiles", description = "고객 추가정보(첨부문서) 목록 조회")
    fun getFiles(
        @PathVariable custAddInfoId: Long
    ): Flow<CustAtchFileResponse> {
        val searchParam = CustAtchFileSearchParam(custAddInfoId)
        return custAtchFileUseCase.getFiles(searchParam)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "saveFile", description = "고객 추가정보(첨부문서) 저장")
    suspend fun saveFile(
        @RequestBody command: CustAtchFileCommand,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): CustAtchFileResponse {
        return custAtchFileUseCase.saveFile(command, auth.adminId)
    }

    @DeleteMapping("/{custAtchFileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleteFile", description = "고객 추가정보(첨부문서) 삭제")
    suspend fun deleteFile(
        @PathVariable custAtchFileId: String
    ) {
        custAtchFileUseCase.deleteFile(custAtchFileId)
    }
}
