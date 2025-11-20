package com.idrsys.ailis.sales.application.required.repository.testCodeMapping

import com.idrsys.ailis.sales.application.dto.query.TestCodeMappingQuery
import com.idrsys.ailis.sales.application.dto.request.testCodeMapping.TestCodeMappingSearchParam
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface TestCodeMappingCustomRepository {
    suspend fun countTestCodeMapping(searchParam: TestCodeMappingSearchParam): Long
    fun findTestCodeMappings(searchParam: TestCodeMappingSearchParam, pageable: Pageable): Flow<TestCodeMappingQuery>
}