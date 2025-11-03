package com.idrsys.ailis.sales

import com.idrsys.reactive.excel.EnableReactiveExcel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableReactiveExcel
class SalesServiceApplication

fun main(args: Array<String>) {
    runApplication<SalesServiceApplication>(*args)
}
