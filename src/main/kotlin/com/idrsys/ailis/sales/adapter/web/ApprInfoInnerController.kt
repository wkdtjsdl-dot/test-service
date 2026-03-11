package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.request.apprinfo.ApprInfoApproveRequest
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
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/approver")
    @Operation(summary = "findApprover", description = "결재정보번호 + 결재순번으로 결재자 userId 조회")
    suspend fun findApprover(
        @RequestParam apprInfoNo: Long,
        @RequestParam apprSeq: Int
    ): String? {
        return apprInfoInnerUseCase.findApproverByApprInfoNoAndSeq(apprInfoNo, apprSeq)
    }

    @GetMapping("/my-approval-info-nos")
    @Operation(summary = "findMyApprovalInfoNos", description = "내 결재 필터용 appr_info_no 목록 조회")
    suspend fun findMyApprovalInfoNos(
        @RequestParam userId: String,
        @RequestParam apprDocTypeCds: List<String>
    ): List<Long> {
        return apprInfoInnerUseCase.findMyApprovalInfoNos(userId, apprDocTypeCds)
    }

    @PostMapping("/approve")
    @Operation(summary = "approveApprInfo", description = "결재선 승인 처리 (APST_W → APST_C)")
    suspend fun approveApprInfo(
        @RequestBody request: ApprInfoApproveRequest
    ) {
        apprInfoInnerUseCase.approveApprInfo(
            request.apprInfoNo,
            request.apprSeq,
            request.apprMemo,
            request.userId
        )
    }

    @GetMapping("/has-next-approver")
    @Operation(summary = "hasNextApprover", description = "다음 결재자 존재 여부 확인")
    suspend fun hasNextApprover(
        @RequestParam apprInfoNo: Long,
        @RequestParam currentSeq: Int
    ): Boolean {
        return apprInfoInnerUseCase.hasNextApprover(apprInfoNo, currentSeq)
    }
}
