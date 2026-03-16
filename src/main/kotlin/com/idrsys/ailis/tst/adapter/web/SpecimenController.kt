package com.idrsys.ailis.tst.adapter.web

import com.idrsys.ailis.tst.application.dto.*
import com.idrsys.ailis.tst.application.usecase.SpecimenContainerUseCase
import com.idrsys.ailis.tst.application.usecase.SpecimenUseCase
import com.idrsys.ailis.tst.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "Specimen Container", description = "검사 기준정보 검체용기 API")
@RestController
@RequestMapping("/api/bbs/spcm-cntn")
class SpecimenContainerController(
    private val specimenContainerUseCase: SpecimenContainerUseCase
) {

    @Operation(summary = "검사 기준정보 검체용기 목록")
    @GetMapping
    fun getContainers(@RequestParam(required = false) cntnNm: String?): Flux<SpecimenContainerResponse> {
        return mono {
            specimenContainerUseCase.getContainers(cntnNm)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "검사 기준정보 검체용기 조회")
    @GetMapping("/{spcmCntnCd}")
    fun getContainer(@PathVariable spcmCntnCd: String): Mono<SpecimenContainerResponse> {
        return mono {
            specimenContainerUseCase.getContainer(spcmCntnCd)
        }
    }

    @Operation(summary = "검사 기준정보 검체용기 등록")
    @PostMapping
    fun registerContainer(
        @RequestBody request: SpecimenContainerRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<SpecimenContainerResponse> {
        return mono {
            specimenContainerUseCase.registerContainer(request, auth.adminId)
        }
    }

    @Operation(summary = "검사 기준정보 검체용기 수정")
    @PutMapping("/{spcmCntnCd}")
    fun updateContainer(
        @PathVariable spcmCntnCd: String,
        @RequestBody request: SpecimenContainerUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<SpecimenContainerResponse> {
        return mono {
            specimenContainerUseCase.updateContainer(spcmCntnCd, request, auth.adminId)
        }
    }

    @Operation(summary = "검사 기준정보 검체용기 삭제")
    @DeleteMapping("/{spcmCntnCd}")
    fun deleteContainer(
        @PathVariable spcmCntnCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> {
        return mono {
            specimenContainerUseCase.deleteContainer(spcmCntnCd, auth.adminId)
        }.then()
    }
}

@Tag(name = "Specimen", description = "검사 기준정보 검체 API")
@RestController
@RequestMapping("/api/bbs/spcm")
class SpecimenController(
    private val specimenUseCase: SpecimenUseCase
) {

    @Operation(summary = "검사 기준정보 검체 목록")
    @GetMapping
    fun getSpecimens(
        @RequestParam(required = false) spcmNm: String?,
        @RequestParam(required = false) spcmCateCd: String?
    ): Flux<SpecimenResponse> {
        return mono {
            specimenUseCase.getSpecimens(spcmNm, spcmCateCd)
        }.flatMapMany { flow -> Flux.from(flow.asPublisher()) }
    }

    @Operation(summary = "검사 기준정보 검체 조회")
    @GetMapping("/{spcmCd}")
    fun getSpecimen(@PathVariable spcmCd: String): Mono<SpecimenResponse> {
        return mono {
            specimenUseCase.getSpecimen(spcmCd)
        }
    }

    @Operation(summary = "검사 기준정보 검체 등록")
    @PostMapping
    fun registerSpecimen(
        @RequestBody request: SpecimenRegisterRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<SpecimenResponse> {
        return mono {
            specimenUseCase.registerSpecimen(request, auth.adminId)
        }
    }

    @Operation(summary = "검사 기준정보 검체 수정")
    @PutMapping("/{spcmCd}")
    fun updateSpecimen(
        @PathVariable spcmCd: String,
        @RequestBody request: SpecimenUpdateRequest,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<SpecimenResponse> {
        return mono {
            specimenUseCase.updateSpecimen(spcmCd, request, auth.adminId)
        }
    }

    @Operation(summary = "검사 기준정보 검체 삭제")
    @DeleteMapping("/{spcmCd}")
    fun deleteSpecimen(
        @PathVariable spcmCd: String,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Mono<Void> {
        return mono {
            specimenUseCase.deleteSpecimen(spcmCd, auth.adminId)
        }.then()
    }
}
