package com.idrsys.ailis.sales

import com.idrsys.reactive.excel.EnableReactiveExcel
import com.idrsys.reactive.exception.EnableReactiveExceptionHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableReactiveExcel
@EnableReactiveExceptionHandler
class SalesServiceApplication

fun main(args: Array<String>) {
    runApplication<SalesServiceApplication>(*args)
}
