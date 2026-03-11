package com.idrsys.ailis.sales.adapter.web

import com.idrsys.ailis.sales.application.dto.response.*
import com.idrsys.ailis.sales.application.usecase.custLogs.CustLogsUseCase
import com.idrsys.ailis.sales.domain.model.Cust
import com.idrsys.ailis.sales.domain.model.CustMstHst
import com.idrsys.ailis.sales.shared.vo.AuthenticationAdmin
import com.idrsys.web.annotation.JwtAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/custs/logs")
@Tag(name = "CustLogsController", description = "고객 변경이력 Controller" )
class CustLogsController(
    private val custLogsUseCase: CustLogsUseCase
) {

    @GetMapping
    @Operation(summary = "getCustomerLogList" ,description = "고객 변경이력 목록")
    suspend fun getCustomerLogList(
        @ParameterObject @Parameter(hidden = true) searchParam: CustLogsSearchParam,
    ): List<CustLogsEditResponse> {
        return custLogsUseCase.getCustomerLogList(searchParam)
    }

    @GetMapping("/{logId}")
    @Operation(summary = "getCustomerLogDetail" ,description = "고객 변경이력 상세 조회")
    suspend fun getCustomerLogDetail(
        @PathVariable logId: String
    ): List<CustDetailLogs> {
        return custLogsUseCase.getCustomerLogDetail(logId)
    }

    @GetMapping("/recover/{logId}")
    @Operation(summary = "getCustomerLogRecover" ,description = "고객 변경이력 복원 상세정보 조회")
    suspend fun getCustomerLogRecover(
        @PathVariable logId: String
    ): CustMstHst {
        return custLogsUseCase.getCustomerLogRecover(logId)
    }

    @PutMapping("/recover/{logId}")
    @Operation(summary = "putCustomerLogRecover" ,description = "고객 변경이력 복원 처리")
    suspend fun putCustomerLogRecover(
        @PathVariable logId: Long,
        @JwtAuthorization @Parameter(hidden = true) auth: AuthenticationAdmin
    ): Cust {
        return custLogsUseCase.putCustomerLogRecover(logId, auth.adminId)
    }
}