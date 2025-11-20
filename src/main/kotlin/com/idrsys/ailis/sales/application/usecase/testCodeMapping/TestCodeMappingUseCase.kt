package com.idrsys.ailis.sales.application.usecase.testCodeMapping

import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingCommand
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingExcelValidResponse
import com.idrsys.ailis.sales.application.dto.response.TestCodeMappingResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TestCodeMappingUseCase {
    suspend fun getTestCodeMappingList(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Page<TestCodeMappingResponse>

    suspend fun createTestCodeMapping(command: TestCodeMappingCommand, adminId: String): TestCodeMappingResponse

    suspend fun validTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>): Flow<TestCodeMappingExcelValidResponse>

    suspend fun createTestCodeMappingByExcel(commands: List<TestCodeMappingCommand>, adminId: String): Flow<TestCodeMappingResponse>
}
