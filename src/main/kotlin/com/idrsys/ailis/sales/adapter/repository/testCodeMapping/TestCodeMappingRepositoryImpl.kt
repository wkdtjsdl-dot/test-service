package com.idrsys.ailis.sales.adapter.repository.testCodeMapping

import com.idrsys.ailis.sales.application.required.repository.testCodeMapping.TestCodeMappingRepository
import com.idrsys.ailis.sales.domain.model.CustTestCodeMapping
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

import kotlinx.coroutines.flow.toList

@Repository
interface CustTestCodeDataRepository : CoroutineCrudRepository<CustTestCodeMapping, String>

@Repository
class TestCodeMappingRepositoryImpl(
    private val custTestCodeDataRepository: CustTestCodeDataRepository
) : TestCodeMappingRepository {
    override suspend fun save(custTestCodeMapping: CustTestCodeMapping): CustTestCodeMapping{
        return custTestCodeDataRepository.save(custTestCodeMapping)
    }

    override suspend fun saveAll(custTestCodeMappings: MutableList<CustTestCodeMapping>): MutableList<CustTestCodeMapping> {
        return custTestCodeDataRepository.saveAll(custTestCodeMappings).toList().toMutableList()
    }
}
