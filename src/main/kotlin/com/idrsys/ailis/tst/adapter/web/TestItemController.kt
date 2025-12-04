package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.TestItemUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "Test Item", description = "검사 아이템 관리 API")
@RestController
class TestItemController(
    private val useCase: TestItemUseCase
) {

    // --- TestItem ---

    @Operation(summary = "검사 검사종목 기본정보 등록")
    @PostMapping("/api/bts/item/base")
    fun registerItem(
        @RequestBody request: TestItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.registerItem(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 기본정보 조회")
    @GetMapping("/api/bts/item/base/{tstCd}")
    fun getItem(@PathVariable tstCd: String): Mono<TestItemResponse> = mono {
        useCase.getItem(tstCd)
    }

    @Operation(summary = "검사 검사종목 기본정보 수정")
    @PutMapping("/api/bts/item/base/{tstCd}")
    fun updateItem(
        @PathVariable tstCd: String,
        @RequestBody request: TestItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemResponse> = mono {
        useCase.updateItem(tstCd, request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 목록")
    @GetMapping("/api/bts/item")
    fun getItems(@ParameterObject searchParam: TestItemSearchParam): Flow<TestItemSimpleResponse> {
        return useCase.getItems(searchParam)
    }

    // --- StandardCharge ---

    @Operation(summary = "검사 검사종목 기준수가 등록")
    @PostMapping("/api/bts/item/stnd-charge")
    fun registerCharge(
        @RequestBody request: StandardChargeRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<StandardChargeResponse> = mono {
        useCase.registerCharge(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 기준수가 조회")
    @GetMapping("/api/bts/item/stnd-charge/{stndChargeId}")
    fun getCharge(@PathVariable stndChargeId: String): Mono<StandardChargeResponse> = mono {
        useCase.getCharge(stndChargeId)
    }

    @Operation(summary = "표준 수가 삭제")
    @DeleteMapping("/api/bts/item/stnd-charge/{stndChargeId}")
    fun deleteCharge(
        @PathVariable stndChargeId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteCharge(stndChargeId, auth.adminId)
        null
    }

    @Operation(summary = "검사 검사종목 기준수가 목록")
    @GetMapping("/api/bts/item/stnd-charge")
    fun getChargesByTest(@RequestParam tstCd: String): Flow<StandardChargeResponse> {
        return kotlinx.coroutines.flow.flow {
            useCase.getChargesByTest(tstCd).collect { emit(it) }
        }
    }

    // --- TestItemSpecimen ---

    @Operation(summary = "검사 검사종목 검체 등록")
    @PostMapping("/api/bts/spcm")
    fun registerSpecimen(
        @RequestBody request: TestItemSpecimenRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemSpecimenResponse> = mono {
        useCase.registerSpecimen(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 검체 조회")
    @GetMapping("/api/bts/spcm/{spcmId}")
    fun getSpecimen(@PathVariable spcmId: String): Mono<TestItemSpecimenResponse> = mono {
        useCase.getSpecimen(spcmId)
    }

    @Operation(summary = "검사 항목별 검체 삭제")
    @DeleteMapping("/api/bts/spcm/{spcmId}")
    fun deleteSpecimen(
        @PathVariable spcmId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteSpecimen(spcmId, auth.adminId)
        null
    }

    @Operation(summary = "검사 검사종목 검체 목록")
    @GetMapping("/api/bts/spcm")
    fun getSpecimensByTest(@RequestParam tstCd: String): Flow<TestItemSpecimenResponse> =
        useCase.getSpecimensByTest(tstCd)

    // --- TestItemRefItem ---

    @Operation(summary = "검사 검사종목 참조항목 등록")
    @PostMapping("/api/bts/ref-item")
    fun registerRefItem(
        @RequestBody request: TestItemRefItemRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemRefItemResponse> = mono {
        useCase.registerRefItem(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 참조항목 조회")
    @GetMapping("/api/bts/ref-item/{refItemId}")
    fun getRefItem(@PathVariable refItemId: String): Mono<TestItemRefItemResponse> = mono {
        useCase.getRefItem(refItemId)
    }

    @Operation(summary = "검사 검사종목 참조항목 수정")
    @PutMapping("/api/bts/ref-item/{refItemId}")
    fun updateRefItem(
        @PathVariable refItemId: String,
        @RequestBody request: TestItemRefItemUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemRefItemResponse> = mono {
        useCase.updateRefItem(refItemId, request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 참조항목 삭제")
    @DeleteMapping("/api/bts/ref-item/{refItemId}")
    fun deleteRefItem(
        @PathVariable refItemId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteRefItem(refItemId, auth.adminId)
        null
    }

    @Operation(summary = "검사 검사종목 참조항목 목록")
    @GetMapping("/api/bts/ref-item")
    fun getRefItemsByTstCd(@RequestParam tstCd: String): Flow<TestItemRefItemResponse> =
        useCase.getRefItemsByTstCd(tstCd)

    // --- TestItemGene ---

    @Operation(summary = "검사 검사종목 유전자 등록")
    @PostMapping("/api/bts/item-gene")
    fun registerGene(
        @RequestBody request: TestItemGeneRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemGeneResponse> = mono {
        useCase.registerGene(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 유전자 삭제")
    @DeleteMapping("/api/bts/item-gene/{itemGeneId}")
    fun deleteGene(
        @PathVariable itemGeneId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteGene(itemGeneId, auth.adminId)
        null
    }

    @Operation(summary = "검사 검사종목 유전자 목록")
    @GetMapping("/api/bts/item-gene")
    fun getGenesByTest(@RequestParam tstCd: String): Flow<TestItemGeneResponse> =
        useCase.getGenesByTest(tstCd)

    // --- TestItemEssentialDoc ---

    @Operation(summary = "검사 검사종목 필수서류 등록")
    @PostMapping("/api/bts/item-estl-doc")
    fun registerEssentialDoc(
        @RequestBody request: TestItemEssentialDocRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemEssentialDocResponse> = mono {
        useCase.registerEssentialDoc(request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 필수서류 조회")
    @GetMapping("/api/bts/item-estl-doc/{itemEstlDocId}")
    fun getEssentialDoc(@PathVariable itemEstlDocId: String): Mono<TestItemEssentialDocResponse> = mono {
        useCase.getEssentialDoc(itemEstlDocId)
    }

    @Operation(summary = "검사 검사종목 필수서류 수정")
    @PutMapping("/api/bts/item-estl-doc/{itemEstlDocId}")
    fun updateEssentialDoc(
        @PathVariable itemEstlDocId: String,
        @RequestBody request: TestItemEssentialDocUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<TestItemEssentialDocResponse> = mono {
        useCase.updateEssentialDoc(itemEstlDocId, request, auth.adminId)
    }

    @Operation(summary = "검사 검사종목 필수서류 삭제")
    @DeleteMapping("/api/bts/item-estl-doc/{itemEstlDocId}")
    fun deleteEssentialDoc(
        @PathVariable itemEstlDocId: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> = mono {
        useCase.deleteEssentialDoc(itemEstlDocId, auth.adminId)
        null
    }

    @Operation(summary = "검사 검사종목 필수서류 목록")
    @GetMapping("/api/bts/item-estl-doc")
    fun getEssentialDocsByTest(@RequestParam tstCd: String): Flow<TestItemEssentialDocResponse> =
        useCase.getEssentialDocsByTest(tstCd)
}
