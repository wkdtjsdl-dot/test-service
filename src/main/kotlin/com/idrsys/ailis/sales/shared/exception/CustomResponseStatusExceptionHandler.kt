package com.idrsys.ailis.sales.shared.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestControllerAdvice
class CustomResponseStatusExceptionHandler {

    /**
     * ResponseStatusException (ServerWebInputException 포함) 발생 시
     * 예외에 설정된 상태 코드와 메시지를 최우선으로 반환합니다.
     */
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<Map<String, Any>> {
        val status = e.statusCode
        val statusCode = status.value()
        
        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to statusCode,
            "error" to (HttpStatus.resolve(statusCode)?.reasonPhrase ?: "Error"),
            "message" to (e.reason ?: e.message),
            "type" to "/errors/status-exception"
        )
        
        return ResponseEntity.status(status).body(body)
    }
}
