package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.TestReferenceSimpleResponse
import com.idrsys.ailis.tst.application.usecase.TestReferenceUseCase
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/bbs/tst-ref")
class TestReferenceInnerController(
    private val testReferenceUseCase: TestReferenceUseCase
) {

    @Operation(summary = "검사 참조항목 inner 조회")
    @GetMapping
    suspend fun findTestReferenceByRefCode(
        @RequestParam cds: String
    ): Flow<TestReferenceSimpleResponse> {
        val refCds = cds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return testReferenceUseCase.findSimpleReferenceByRefCd(refCds)
    }
}