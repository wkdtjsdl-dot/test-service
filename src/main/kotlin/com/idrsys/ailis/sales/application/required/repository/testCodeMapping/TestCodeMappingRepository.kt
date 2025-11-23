package com.idrsys.ailis.sales.application.required.repository.testCodeMapping

import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping

interface TestCodeMappingRepository {
    suspend fun save(custTestCodeMapping: CustTestCodeMapping): CustTestCodeMapping
    suspend fun saveAll(custTestCodeMappings: MutableList<CustTestCodeMapping>): MutableList<CustTestCodeMapping>
}