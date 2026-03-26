package com.idrsys.ailis.sales.shared.exception

import com.idrsys.ailis.sales.shared.constant.CustReqPossTstItemErrorCode
import com.idrsys.web.exception.UserDefinedException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserDefinedExceptionHandler {

    @ExceptionHandler(UserDefinedException::class)
    suspend fun handleUserDefined(ex: UserDefinedException): ProblemDetail {
        val status = when (ex.errorCode) {
            CustReqPossTstItemErrorCode.TST_CD_NOT_FOUND_CODE,
            CustReqPossTstItemErrorCode.NOT_FOUND_CODE -> HttpStatus.NOT_FOUND       // 404
            CustReqPossTstItemErrorCode.DUPLICATE_CODE -> HttpStatus.CONFLICT        // 409
            else -> HttpStatus.INTERNAL_SERVER_ERROR                                 // 500 fallback
        }

        return ProblemDetail.forStatusAndDetail(
            status,
            ex.message ?: "User defined error occurred"
        ).apply {
            title = "User Defined Error"
            type = URI.create("/errors/user-defined")
            if (ex.errorCode.isNotEmpty()) setProperty("errorCode", ex.errorCode)
        }
    }
}
