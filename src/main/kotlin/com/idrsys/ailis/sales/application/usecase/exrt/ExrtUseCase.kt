package com.idrsys.ailis.sales.application.usecase.exrt

import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtBatchCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtCommand
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtSearchParam
import com.idrsys.ailis.sales.application.dto.request.exrt.ExrtUpdateCommand
import com.idrsys.ailis.sales.application.dto.response.ExrtBatchResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtListResponse
import com.idrsys.ailis.sales.application.dto.response.ExrtResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface ExrtUseCase {
    suspend fun batchRegisterExrt(commands: List<ExrtBatchCommand>): ExrtBatchResponse
    suspend fun getExrtPage(searchParam: ExrtSearchParam, pageable: Pageable): Page<ExrtListResponse>
    suspend fun getExrtsBy(stndDt: LocalDate?, crcyCd: String?): List<ExrtResponse>
    suspend fun getExrtDetail(exrtId: Long): ExrtResponse
    suspend fun updateExrt(exrtId: Long, command: ExrtUpdateCommand, adminId: String): ExrtResponse
    suspend fun createExrt(command: ExrtCommand, adminId: String): ExrtResponse
    suspend fun deleteExrt(exrtId: Long)
}
