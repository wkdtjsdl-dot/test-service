package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.RequestDocumentRegisterRequest
import com.idrsys.ailis.tst.application.dto.RequestDocumentResponse
import com.idrsys.ailis.tst.application.dto.RequestDocumentUpdateRequest
import com.idrsys.ailis.tst.application.usecase.RequestDocumentUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "Request Document", description = "검사 기준정보 의뢰서서류 API")
@RestController
@RequestMapping("/api/tst/bbs/req-doc")
class RequestDocumentController(
    private val requestDocumentUseCase: RequestDocumentUseCase
) {

    @Operation(summary = "의뢰서서류 목록")
    @GetMapping
    fun getDocuments(@RequestParam(required = false) docDivCd: String?): Flux<RequestDocumentResponse> {
        return mono {
            requestDocumentUseCase.getDocuments(docDivCd)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "의뢰서서류 조회")
    @GetMapping("/{docCd}")
    fun getDocument(@PathVariable docCd: String): Mono<RequestDocumentResponse> {
        return mono {
            requestDocumentUseCase.getDocument(docCd)
        }
    }

    @Operation(summary = "의뢰서서류 등록")
    @PostMapping
    fun registerDocument(@RequestBody request: RequestDocumentRegisterRequest): Mono<RequestDocumentResponse> {
        return mono {
            requestDocumentUseCase.registerDocument(request)
        }
    }

    @Operation(summary = "의뢰서서류 수정")
    @PutMapping("/{docCd}")
    fun updateDocument(
        @PathVariable docCd: String,
        @RequestBody request: RequestDocumentUpdateRequest
    ): Mono<RequestDocumentResponse> {
        return mono {
            requestDocumentUseCase.updateDocument(docCd, request)
        }
    }

    @Operation(summary = "의뢰서서류 삭제")
    @DeleteMapping("/{docCd}")
    fun deleteDocument(@PathVariable docCd: String): Mono<Void> {
        return mono {
            requestDocumentUseCase.deleteDocument(docCd)
        }.then()
    }
}
