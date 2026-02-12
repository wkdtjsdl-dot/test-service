package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.apprinfo.ApprInfoCreateRequest
import com.idrsys.ailis.sales.application.usecase.apprinfo.ApprInfoInnerUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/appr-info")
@Tag(name = "ApprInfoInnerController", description = "결재정보 Inner API Controller")
class ApprInfoInnerController(
    private val apprInfoInnerUseCase: ApprInfoInnerUseCase
) {
    @GetMapping("/next-seq")
    @Operation(summary = "getNextApprInfoNo", description = "다음 결재정보번호 시퀀스 조회")
    suspend fun getNextApprInfoNo(): Long {
        return apprInfoInnerUseCase.getNextApprInfoNo()
    }

    @PostMapping("/batch")
    @Operation(summary = "saveApprInfoBatch", description = "결재라인 일괄 저장")
    suspend fun saveApprInfoBatch(
        @RequestBody requests: List<ApprInfoCreateRequest>
    ) {
        apprInfoInnerUseCase.saveApprInfoBatch(requests)
    }

    @DeleteMapping("/{apprInfoNo}")
    @Operation(summary = "deleteByApprInfoNo", description = "결재라인 삭제 (apprInfoNo로 전체 삭제)")
    suspend fun deleteByApprInfoNo(
        @PathVariable apprInfoNo: Long
    ) {
        apprInfoInnerUseCase.deleteByApprInfoNo(apprInfoNo)
    }
}
