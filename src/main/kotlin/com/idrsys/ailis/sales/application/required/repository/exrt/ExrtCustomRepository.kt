package com.idrsys.ailis.sales.application.required.repository.exrt

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtBatchCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtSearchParam
import com.idrsys.ailis.sales.domain.model.Exrt
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface ExrtCustomRepository {
    suspend fun batchInsert(commands: List<ExrtBatchCommand>, creator: String): Int
    fun findExrts(searchParam: ExrtSearchParam, pageable: Pageable?): Flow<Exrt>
    suspend fun countExrts(searchParam: ExrtSearchParam): Long
    suspend fun findExrtById(exrtId: Long): Exrt?
}
