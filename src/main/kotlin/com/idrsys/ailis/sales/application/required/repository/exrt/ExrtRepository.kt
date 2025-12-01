package com.idrsys.ailis.sales.application.required.repository.exrt

import com.idrsys.ailis.sales.domain.model.Exrt
import java.time.LocalDate

interface ExrtRepository {
    suspend fun save(exrt: Exrt): Exrt
    suspend fun findById(id: Long): Exrt?
    suspend fun deleteById(id: Long)
    suspend fun existsByStndDtAndCrcyCd(stndDt: LocalDate, crcyCd: String): Boolean
}
