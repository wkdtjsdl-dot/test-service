package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtBatchCommand
import com.idrsys.ailis.sales.application.dto.response.ExrtBatchResponse
import com.idrsys.ailis.sales.application.usecase.exrt.ExrtUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inner/custs/exrt")
@Tag(name = "ExrtInnerController", description = "환율 내부 Controller")
class ExrtInnerController(
    private val exrtUseCase: ExrtUseCase
) {

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "batchRegisterExrt", description = "환율 배치 등록")
    suspend fun batchRegisterExrt(
        @RequestBody commands: List<ExrtBatchCommand>
    ): ExrtBatchResponse {
        return exrtUseCase.batchRegisterExrt(commands)
    }
}
